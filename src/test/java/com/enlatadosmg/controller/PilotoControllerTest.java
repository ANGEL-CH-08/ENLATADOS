package com.enlatadosmg.controller;

import com.enlatadosmg.model.Piloto;
import com.enlatadosmg.service.PilotoService;
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

public class PilotoControllerTest {

    @Mock  private PilotoService pilotoService;
    @InjectMocks private PilotoController controller;

    private Piloto piloto;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        piloto = new Piloto("001", "Juan", "Morales", "A", "55551234");
    }

    @Test
    public void registrar_pilotoValido_retorna200() {
        when(pilotoService.registrar(any())).thenReturn(piloto);
        assertEquals(HttpStatus.OK, controller.registrar(piloto).getStatusCode());
    }

    @Test
    public void registrar_cuiDuplicado_retorna400() {
        when(pilotoService.registrar(any())).thenThrow(new RuntimeException("Ya existe un piloto registrado con el CUI '001'"));
        assertEquals(HttpStatus.BAD_REQUEST, controller.registrar(piloto).getStatusCode());
    }

    @Test
    public void listar_retornaLista() {
        when(pilotoService.obtenerTodos()).thenReturn(List.of(piloto));
        ResponseEntity<List<Piloto>> res = controller.listar();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    public void disponibles_retornaCantidades() {
        when(pilotoService.getCantidadLibres()).thenReturn(1);
        when(pilotoService.getCantidad()).thenReturn(2);
        ResponseEntity<Map<String, Integer>> res = controller.disponibles();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(Integer.valueOf(1), res.getBody().get("libres"));
        assertEquals(Integer.valueOf(2), res.getBody().get("total"));
    }

    @Test
    public void editar_pilotoExistente_retorna200() {
        when(pilotoService.editar("001", "Carlos", null, null, null)).thenReturn(piloto);
        assertEquals(HttpStatus.OK, controller.editar("001", Map.of("nombre", "Carlos")).getStatusCode());
    }

    @Test
    public void editar_pilotoInexistente_retorna400() {
        when(pilotoService.editar(any(), any(), any(), any(), any()))
            .thenThrow(new RuntimeException("No existe ningun piloto con el CUI: 999"));
        assertEquals(HttpStatus.BAD_REQUEST, controller.editar("999", Map.of("nombre", "X")).getStatusCode());
    }

    @Test
    public void cambiarEstado_exitoso_retorna200() {
        assertEquals(HttpStatus.OK, controller.cambiarEstado("001", Map.of("estado", "DE_VACACIONES")).getStatusCode());
        verify(pilotoService).cambiarEstado("001", "DE_VACACIONES");
    }

    @Test
    public void cambiarEstado_pilotoOcupado_retorna400() {
        doThrow(new RuntimeException("El piloto esta OCUPADO con un pedido activo."))
            .when(pilotoService).cambiarEstado(any(), any());
        assertEquals(HttpStatus.BAD_REQUEST, controller.cambiarEstado("001", Map.of("estado", "LIBRE")).getStatusCode());
    }
}
