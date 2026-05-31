package com.enlatadosmg.service;

import com.enlatadosmg.model.CatalogoDeProductos;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class CatalogoServiceTest {

    private DataStore       dataStore;
    private CatalogoService service;

    @Before
    public void setUp() {
        dataStore = new DataStore();
        service   = new CatalogoService(dataStore);
    }

    @Test
    public void registrarProducto_codigoNuevo_seAgregaAlCatalogo() {
        CatalogoDeProductos p = service.registrarProducto("PROD-001", "Atun", "desc", 1.5, 24, 50.0);
        assertNotNull(p);
        assertEquals("PROD-001", p.getCodigoProducto());
        assertEquals(0, p.getCajasEnStock());
    }

    @Test(expected = RuntimeException.class)
    public void registrarProducto_codigoDuplicado_lanzaExcepcion() {
        service.registrarProducto("PROD-001", "Atun",  "desc", 1.5, 24, 50.0);
        service.registrarProducto("PROD-001", "Otro",  "desc2", 1.0, 12, 30.0);
    }

    @Test
    public void registrarProducto_codigoEnMinusculas_seNormalizaAMayusculas() {
        service.registrarProducto("prod-002", "Sardina", "desc", 1.0, 12, 30.0);
        assertNotNull(service.buscarPorCodigo("PROD-002"));
    }

    @Test
    public void buscarPorCodigo_existente_retornaProducto() {
        service.registrarProducto("PROD-001", "Atun", "desc", 1.5, 24, 50.0);
        CatalogoDeProductos encontrado = service.buscarPorCodigo("PROD-001");
        assertNotNull(encontrado);
        assertEquals("Atun", encontrado.getNombreProducto());
    }

    @Test
    public void buscarPorCodigo_inexistente_retornaNull() {
        assertNull(service.buscarPorCodigo("NO-EXISTE"));
    }

    @Test
    public void buscarPorTexto_coincideParcial_retornaResultados() {
        service.registrarProducto("PROD-001", "Atun en aceite", "desc", 1.5, 24, 50.0);
        service.registrarProducto("PROD-002", "Sardina",        "desc", 1.0, 12, 30.0);
        List<CatalogoDeProductos> resultados = service.buscarPorTexto("atun");
        assertEquals(1, resultados.size());
        assertEquals("PROD-001", resultados.get(0).getCodigoProducto());
    }

    @Test
    public void obtenerConStock_soloRetornaProductosConCajas() {
        CatalogoDeProductos p1 = service.registrarProducto("P1", "Atun",    "d", 1.0, 12, 10.0);
        service.registrarProducto("P2", "Sardina", "d", 1.0, 12, 10.0);
        p1.sumarCajas(10);
        List<CatalogoDeProductos> conStock = service.obtenerConStock();
        assertEquals(1, conStock.size());
        assertEquals("P1", conStock.get(0).getCodigoProducto());
    }
}
