package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.CajaDeProductos;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class PilaDeAlmacenTest {

    private PilaDeAlmacen pila;

    @Before
    public void setUp() {
        pila = new PilaDeAlmacen();
    }

    private CajaDeProductos caja(int id, String codigo, int cantidad) {
        return new CajaDeProductos(id, codigo, "CAJA-" + id, "Producto", cantidad, 1.0, "A", 1, 1, 1);
    }

    @Test
    public void apilarOSumar_cajaNueva_incrementaRegistros() {
        pila.apilarOSumar(caja(1, "PROD-001", 100));
        assertEquals(1, pila.getTotalRegistros());
    }

    @Test
    public void apilarOSumar_mismaUbicacionYProducto_sumaEnLugarDeApilar() {
        pila.apilarOSumar(caja(1, "PROD-001", 50));
        pila.apilarOSumar(caja(2, "PROD-001", 30));
        assertEquals(1, pila.getTotalRegistros());
        assertEquals(80, pila.contarCajasPorProducto("PROD-001"));
    }

    @Test
    public void apilarOSumar_diferenteProducto_creaRegistroNuevo() {
        pila.apilarOSumar(caja(1, "PROD-001", 50));
        pila.apilarOSumar(caja(2, "PROD-002", 30));
        assertEquals(2, pila.getTotalRegistros());
    }

    @Test
    public void retirarCajas_stockSuficiente_reduceConteo() {
        pila.apilarOSumar(caja(1, "PROD-001", 100));
        List<CajaDeProductos> retiradas = pila.retirarCajasPorProducto("PROD-001", 40);
        assertFalse(retiradas.isEmpty());
        assertEquals(60, pila.contarCajasPorProducto("PROD-001"));
    }

    @Test(expected = RuntimeException.class)
    public void retirarCajas_stockInsuficiente_lanzaExcepcion() {
        pila.apilarOSumar(caja(1, "PROD-001", 10));
        pila.retirarCajasPorProducto("PROD-001", 500);
    }

    @Test
    public void cambiarEstado_idExistente_retornaTrue() {
        pila.apilarOSumar(caja(1, "PROD-001", 100));
        assertTrue(pila.cambiarEstado(1, "RESERVADO"));
    }

    @Test
    public void cambiarEstado_idInexistente_retornaFalse() {
        assertFalse(pila.cambiarEstado(999, "RESERVADO"));
    }

    @Test
    public void estaVacio_pilaVacia_retornaTrue() {
        assertTrue(pila.estaVacio());
    }
}
