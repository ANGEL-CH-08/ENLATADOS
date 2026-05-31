package com.enlatadosmg.controller;

import com.enlatadosmg.model.CatalogoDeProductos;
import com.enlatadosmg.service.CatalogoService;
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

public class CatalogoControllerTest {

    @Mock  private CatalogoService catalogoService;
    @InjectMocks private CatalogoController controller;

    private CatalogoDeProductos producto;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        producto = new CatalogoDeProductos("PROD-001", "Atun", "desc", 1.5, 24, 50.0);
    }

    @Test
    public void registrar_productoValido_retorna200() {
        when(catalogoService.registrarProducto(any(), any(), any(), anyDouble(), anyInt(), anyDouble())).thenReturn(producto);
        assertEquals(HttpStatus.OK, controller.registrar(Map.of(
            "codigoProducto", "PROD-001", "nombreProducto", "Atun",
            "pesoKgPorCaja", 1.5, "unidadesPorCaja", 24, "precioUnitario", 50.0)).getStatusCode());
    }

    @Test
    public void registrar_codigoDuplicado_retorna400() {
        when(catalogoService.registrarProducto(any(), any(), any(), anyDouble(), anyInt(), anyDouble()))
            .thenThrow(new RuntimeException("Ya existe un producto con el codigo: PROD-001"));
        assertEquals(HttpStatus.BAD_REQUEST, controller.registrar(Map.of(
            "codigoProducto", "PROD-001", "nombreProducto", "Atun",
            "pesoKgPorCaja", 1.5, "unidadesPorCaja", 24)).getStatusCode());
    }

    @Test
    public void listar_retornaListaCompleta() {
        when(catalogoService.obtenerTodos()).thenReturn(List.of(producto));
        ResponseEntity<List<CatalogoDeProductos>> res = controller.listar();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    public void listarConStock_soloProductosConCajas() {
        producto.sumarCajas(10);
        when(catalogoService.obtenerConStock()).thenReturn(List.of(producto));
        ResponseEntity<List<CatalogoDeProductos>> res = controller.listarConStock();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    public void obtener_codigoExistente_retorna200() {
        when(catalogoService.buscarPorCodigo("PROD-001")).thenReturn(producto);
        assertEquals(HttpStatus.OK, controller.obtener("PROD-001").getStatusCode());
    }

    @Test
    public void obtener_codigoInexistente_retorna404() {
        when(catalogoService.buscarPorCodigo("NO-EXISTE")).thenReturn(null);
        assertEquals(HttpStatus.NOT_FOUND, controller.obtener("NO-EXISTE").getStatusCode());
    }

    @Test
    public void buscar_textoParcial_retornaResultados() {
        when(catalogoService.buscarPorTexto("atun")).thenReturn(List.of(producto));
        ResponseEntity<List<CatalogoDeProductos>> res = controller.buscar("atun");
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }
}
