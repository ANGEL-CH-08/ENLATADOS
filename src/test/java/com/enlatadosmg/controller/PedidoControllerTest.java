package com.enlatadosmg.controller;

import com.enlatadosmg.model.*;
import com.enlatadosmg.service.PedidoService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PedidoControllerTest {

    @Mock  private PedidoService pedidoService;
    @InjectMocks private PedidoController controller;

    private PedidoDeEntrega pedido;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Cliente  cli = new Cliente("1111", "Maria", "Perez", "55551234", "Zona 1");
        Piloto   pil = new Piloto("001", "Juan", "Morales", "A", "11111");
        Vehiculo veh = new Vehiculo("P-001", "Toyota", "Hilux", "Blanco", 2022, "Manual", 50);
        pedido = new PedidoDeEntrega(1, "Guatemala", "Quetzaltenango", cli, pil, veh);
    }

    private Map<String, Object> bodyValido() {
        return Map.of(
            "cuiCliente", "1111",
            "departamentoOrigen", "Guatemala",
            "departamentoDestino", "Quetzaltenango",
            "lineas", List.of(Map.of("codigoProducto", "PROD-001", "cantidadCajas", 10))
        );
    }

    @Test
    public void crear_pedidoValido_retorna200() {
        when(pedidoService.crearPedidoMultiProducto(any(), any(), any(), any())).thenReturn(List.of(pedido));
        assertEquals(HttpStatus.OK, controller.crear(bodyValido()).getStatusCode());
    }

    @Test
    public void crear_clienteNoExiste_retorna400() {
        when(pedidoService.crearPedidoMultiProducto(any(), any(), any(), any()))
            .thenThrow(new RuntimeException("Cliente no encontrado con CUI: 9999"));
        assertEquals(HttpStatus.BAD_REQUEST, controller.crear(bodyValido()).getStatusCode());
    }

    @Test
    public void crear_stockInsuficiente_retorna400() {
        when(pedidoService.crearPedidoMultiProducto(any(), any(), any(), any()))
            .thenThrow(new RuntimeException("Stock insuficiente de Atun."));
        assertEquals(HttpStatus.BAD_REQUEST, controller.crear(bodyValido()).getStatusCode());
    }

    @Test
    public void listar_retornaListaDePedidos() {
        when(pedidoService.obtenerTodos()).thenReturn(List.of(pedido));
        ResponseEntity<List<PedidoDeEntrega>> res = controller.listar();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    public void obtener_pedidoExistente_retorna200() {
        when(pedidoService.buscarPorNumero(1)).thenReturn(pedido);
        assertEquals(HttpStatus.OK, controller.obtener(1).getStatusCode());
    }

    @Test
    public void obtener_pedidoInexistente_retorna404() {
        when(pedidoService.buscarPorNumero(99)).thenReturn(null);
        assertEquals(HttpStatus.NOT_FOUND, controller.obtener(99).getStatusCode());
    }

    @Test
    public void cambiarEstado_completarPedido_retorna200() {
        when(pedidoService.cambiarEstado(1, "COMPLETADO", "OK")).thenReturn(pedido);
        assertEquals(HttpStatus.OK, controller.cambiarEstado(1, Map.of("estado", "COMPLETADO", "observaciones", "OK")).getStatusCode());
    }

    @Test
    public void cambiarEstado_pedidoYaCompletado_retorna400() {
        when(pedidoService.cambiarEstado(anyInt(), any(), any()))
            .thenThrow(new RuntimeException("El pedido ya fue completado y no puede modificarse"));
        assertEquals(HttpStatus.BAD_REQUEST, controller.cambiarEstado(1, Map.of("estado", "CANCELADO", "observaciones", "")).getStatusCode());
    }
}
