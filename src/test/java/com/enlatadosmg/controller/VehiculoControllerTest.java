package com.enlatadosmg.controller;

import com.enlatadosmg.model.Vehiculo;
import com.enlatadosmg.service.VehiculoService;
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

public class VehiculoControllerTest {

    @Mock  private VehiculoService vehiculoService;
    @InjectMocks private VehiculoController controller;

    private Vehiculo vehiculo;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        vehiculo = new Vehiculo("P-001", "Toyota", "Hilux", "Blanco", 2022, "Manual", 50);
    }

    private Map<String, Object> bodyValido() {
        return Map.of("placa", "P-001", "marca", "Toyota", "modelo", "Hilux",
            "color", "Blanco", "anio", 2022, "tipoTransmision", "Manual", "capacidadMaxCajas", 50);
    }

    @Test
    public void registrar_vehiculoValido_retorna200() {
        when(vehiculoService.registrar(any())).thenReturn(vehiculo);
        assertEquals(HttpStatus.OK, controller.registrar(bodyValido()).getStatusCode());
    }

    @Test
    public void registrar_placaDuplicada_retorna400() {
        when(vehiculoService.registrar(any())).thenThrow(new RuntimeException("Ya existe un vehiculo registrado con la placa 'P-001'"));
        assertEquals(HttpStatus.BAD_REQUEST, controller.registrar(bodyValido()).getStatusCode());
    }

    @Test
    public void listar_retornaLista() {
        when(vehiculoService.obtenerTodos()).thenReturn(List.of(vehiculo));
        ResponseEntity<List<Vehiculo>> res = controller.listar();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    public void disponibles_retornaCantidades() {
        when(vehiculoService.getCantidadLibres()).thenReturn(1);
        when(vehiculoService.getCantidad()).thenReturn(2);
        ResponseEntity<Map<String, Integer>> res = controller.disponibles();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(Integer.valueOf(1), res.getBody().get("libres"));
    }

    @Test
    public void editar_vehiculoExistente_retorna200() {
        when(vehiculoService.editar(any(), any(), any(), any(), any(), any(), any())).thenReturn(vehiculo);
        assertEquals(HttpStatus.OK, controller.editar("P-001", Map.of("color", "Negro")).getStatusCode());
    }

    @Test
    public void editar_vehiculoInexistente_retorna400() {
        when(vehiculoService.editar(any(), any(), any(), any(), any(), any(), any()))
            .thenThrow(new RuntimeException("No existe ningun vehiculo con la placa: P-999"));
        assertEquals(HttpStatus.BAD_REQUEST, controller.editar("P-999", Map.of("color", "Rojo")).getStatusCode());
    }

    @Test
    public void cambiarEstado_exitoso_retorna200() {
        assertEquals(HttpStatus.OK, controller.cambiarEstado("P-001", Map.of("estado", "FUERA_DE_SERVICIO")).getStatusCode());
        verify(vehiculoService).cambiarEstado("P-001", "FUERA_DE_SERVICIO");
    }

    @Test
    public void cambiarEstado_vehiculoOcupado_retorna400() {
        doThrow(new RuntimeException("El vehiculo P-001 esta OCUPADO con un pedido activo."))
            .when(vehiculoService).cambiarEstado(any(), any());
        assertEquals(HttpStatus.BAD_REQUEST, controller.cambiarEstado("P-001", Map.of("estado", "LIBRE")).getStatusCode());
    }
}
