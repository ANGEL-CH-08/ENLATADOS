package com.enlatadosmg.controller;

import com.enlatadosmg.model.Cliente;
import com.enlatadosmg.service.ClienteService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClienteControllerTest {

    @Mock  private ClienteService clienteService;
    @InjectMocks private ClienteController controller;

    private Cliente cliente;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente("1111", "Maria", "Perez", "55551234", "Zona 1");
    }

    @Test
    public void crear_clienteValido_retorna200() {
        when(clienteService.agregar(any())).thenReturn(cliente);
        assertEquals(HttpStatus.OK, controller.crear(cliente).getStatusCode());
    }

    @Test
    public void crear_cuiDuplicado_retorna400() {
        when(clienteService.agregar(any())).thenThrow(new RuntimeException("Ya existe un cliente con el CUI: 1111"));
        assertEquals(HttpStatus.BAD_REQUEST, controller.crear(cliente).getStatusCode());
    }

    @Test
    public void listar_retornaLista() {
        when(clienteService.obtenerTodos()).thenReturn(List.of(cliente));
        ResponseEntity<List<Cliente>> res = controller.listar();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    public void obtener_clienteExistente_retorna200() {
        when(clienteService.buscarPorCui("1111")).thenReturn(cliente);
        assertEquals(HttpStatus.OK, controller.obtener("1111").getStatusCode());
    }

    @Test
    public void obtener_clienteInexistente_retorna404() {
        when(clienteService.buscarPorCui("9999")).thenReturn(null);
        assertEquals(HttpStatus.NOT_FOUND, controller.obtener("9999").getStatusCode());
    }

    @Test
    public void modificar_clienteExistente_retorna200() {
        when(clienteService.buscarPorCui("1111")).thenReturn(cliente);
        assertEquals(HttpStatus.OK, controller.modificar("1111", cliente).getStatusCode());
        verify(clienteService).modificar(cliente);
    }

    @Test
    public void modificar_clienteInexistente_retorna404() {
        when(clienteService.buscarPorCui("9999")).thenReturn(null);
        assertEquals(HttpStatus.NOT_FOUND, controller.modificar("9999", cliente).getStatusCode());
    }

    @Test
    public void eliminar_clienteExistente_retorna200() {
        when(clienteService.eliminar("1111")).thenReturn(true);
        assertEquals(HttpStatus.OK, controller.eliminar("1111").getStatusCode());
    }

    @Test
    public void eliminar_clienteInexistente_retorna404() {
        when(clienteService.eliminar("9999")).thenReturn(false);
        assertEquals(HttpStatus.NOT_FOUND, controller.eliminar("9999").getStatusCode());
    }
}
