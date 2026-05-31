package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.Vehiculo;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ColaDeVehiculosTest {

    private ColaDeVehiculos cola;

    @Before
    public void setUp() {
        cola = new ColaDeVehiculos();
    }

    private Vehiculo v(String placa) {
        return new Vehiculo(placa, "Toyota", "Hilux", "Blanco", 2022, "Manual", 50);
    }

    @Test
    public void registrar_vehiculoNuevo_incrementaTotal() {
        cola.registrar(v("P-001"));
        assertEquals(1, cola.getTotalVehiculos());
    }

    @Test
    public void asignarDisponible_vehiculoLibre_cambiaAOcupado() {
        cola.registrar(v("P-001"));
        Vehiculo asignado = cola.asignarDisponible();
        assertEquals("OCUPADO", asignado.getEstado());
    }

    @Test(expected = RuntimeException.class)
    public void asignarDisponible_sinVehiculosLibres_lanzaExcepcion() {
        cola.asignarDisponible();
    }

    @Test
    public void liberarVehiculo_cambiaALibre() {
        cola.registrar(v("P-001"));
        Vehiculo asignado = cola.asignarDisponible();
        cola.liberarVehiculo(asignado);
        assertEquals("LIBRE", asignado.getEstado());
        assertEquals(1, cola.contarLibres());
    }

    @Test
    public void cambiarEstado_vehiculoExistente_actualizaEstado() {
        cola.registrar(v("P-001"));
        assertTrue(cola.cambiarEstado("P-001", "FUERA_DE_SERVICIO"));
        assertEquals("FUERA_DE_SERVICIO", cola.buscarPorPlaca("P-001").getEstado());
    }

    @Test
    public void contarLibres_soloLosLibres() {
        cola.registrar(v("P-001"));
        cola.registrar(v("P-002"));
        cola.asignarDisponible();
        assertEquals(1, cola.contarLibres());
    }

    @Test
    public void estaVacia_colaVacia_retornaTrue() {
        assertTrue(cola.estaVacia());
    }

    @Test
    public void fifo_asignaEnOrdenDeRegistro() {
        cola.registrar(v("P-001"));
        cola.registrar(v("P-002"));
        Vehiculo primero = cola.asignarDisponible();
        assertEquals("P-001", primero.getPlaca());
    }
}
