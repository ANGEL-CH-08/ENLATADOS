package com.enlatadosmg.service;

import com.enlatadosmg.model.CajaDeProductos;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class AlmacenServiceTest {

    private DataStore      dataStore;
    private CatalogoService catalogoService;
    private AlmacenService  service;

    @Before
    public void setUp() {
        dataStore       = new DataStore();
        catalogoService = new CatalogoService(dataStore);
        service         = new AlmacenService(dataStore, catalogoService);
        catalogoService.registrarProducto("PROD-001", "Atun", "desc", 1.5, 24, 50.0);
    }

    @Test
    public void ingresarCajas_productoValido_apareceEnInventario() {
        CajaDeProductos lote = service.ingresarCajas("PROD-001", 100, "A", 1, 1, 1);
        assertNotNull(lote);
        assertEquals(100, lote.getCantidadCajas());
        assertEquals(1, service.obtenerInventario().size());
    }

    @Test
    public void ingresarCajas_actualizaStockEnCatalogo() {
        service.ingresarCajas("PROD-001", 50, "A", 1, 1, 1);
        assertEquals(50, catalogoService.buscarPorCodigo("PROD-001").getCajasEnStock());
    }

    @Test(expected = RuntimeException.class)
    public void ingresarCajas_productoNoCatalogo_lanzaExcepcion() {
        service.ingresarCajas("NO-EXISTE", 10, "A", 1, 1, 1);
    }

    @Test(expected = RuntimeException.class)
    public void ingresarCajas_cantidadCero_lanzaExcepcion() {
        service.ingresarCajas("PROD-001", 0, "A", 1, 1, 1);
    }

    @Test
    public void retirarCajas_stockSuficiente_reduceInventario() {
        service.ingresarCajas("PROD-001", 100, "A", 1, 1, 1);
        List<CajaDeProductos> retiradas = service.retirarCajasPorProducto("PROD-001", 30);
        assertFalse(retiradas.isEmpty());
        assertEquals(70, dataStore.getPilaDeAlmacen().contarCajasPorProducto("PROD-001"));
    }

    @Test(expected = RuntimeException.class)
    public void retirarCajas_stockInsuficiente_lanzaExcepcion() {
        service.ingresarCajas("PROD-001", 10, "A", 1, 1, 1);
        service.retirarCajasPorProducto("PROD-001", 500);
    }

    @Test
    public void cambiarEstadoLote_idExistente_retornaTrue() {
        service.ingresarCajas("PROD-001", 50, "A", 1, 1, 1);
        int id = service.obtenerInventario().get(0).getId();
        assertTrue(service.cambiarEstadoLote(id, "RESERVADO"));
    }

    @Test
    public void cambiarEstadoLote_idInexistente_retornaFalse() {
        assertFalse(service.cambiarEstadoLote(9999, "RESERVADO"));
    }
}
