package com.enlatadosmg.controller;

import com.enlatadosmg.model.CajaDeProductos;
import com.enlatadosmg.service.AlmacenService;
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

public class AlmacenControllerTest {

    @Mock  private AlmacenService almacenService;
    @InjectMocks private AlmacenController controller;

    private CajaDeProductos caja;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        caja = new CajaDeProductos(1, "PROD-001", "CAJA-0001", "Atun", 100, 1.5, "A", 1, 1, 1);
    }

    private Map<String, Object> bodyValido() {
        return Map.of("codigoProducto", "PROD-001", "cantidadCajas", 100,
            "zona", "A", "pasillo", 1, "estante", 1, "nivel", 1);
    }

    @Test
    public void ingresar_datosValidos_retorna200() {
        when(almacenService.ingresarCajas("PROD-001", 100, "A", 1, 1, 1)).thenReturn(caja);
        assertEquals(HttpStatus.OK, controller.ingresarCajas(bodyValido()).getStatusCode());
    }

    @Test
    public void ingresar_productoNoCatalogo_retorna400() {
        when(almacenService.ingresarCajas(any(), anyInt(), any(), anyInt(), anyInt(), anyInt()))
            .thenThrow(new RuntimeException("Producto no encontrado en el catalogo: PROD-999"));
        assertEquals(HttpStatus.BAD_REQUEST, controller.ingresarCajas(bodyValido()).getStatusCode());
    }

    @Test
    public void listar_retornaInventario() {
        when(almacenService.obtenerInventario()).thenReturn(List.of(caja));
        ResponseEntity<List<CajaDeProductos>> res = controller.listar();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    public void cambiarEstado_idExistente_retorna200() {
        when(almacenService.cambiarEstadoLote(1, "RESERVADO")).thenReturn(true);
        assertEquals(HttpStatus.OK, controller.cambiarEstado(1, Map.of("estado", "RESERVADO")).getStatusCode());
    }

    @Test
    public void cambiarEstado_idInexistente_retorna404() {
        when(almacenService.cambiarEstadoLote(999, "RESERVADO")).thenReturn(false);
        assertEquals(HttpStatus.NOT_FOUND, controller.cambiarEstado(999, Map.of("estado", "RESERVADO")).getStatusCode());
    }
}
