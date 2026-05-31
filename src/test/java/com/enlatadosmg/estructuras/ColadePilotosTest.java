package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.Piloto;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ColadePilotosTest {

    private ColadePilotos cola;

    @Before
    public void setUp() {
        cola = new ColadePilotos();
    }

    private Piloto p(String cui) {
        return new Piloto(cui, "Juan", "Morales", "A", "111");
    }

    @Test
    public void registrar_pilotoNuevo_incrementaTotal() {
        cola.registrar(p("001"));
        assertEquals(1, cola.getTotalPilotos());
    }

    @Test
    public void asignarDisponible_pilotoLibre_cambiaAOcupado() {
        cola.registrar(p("001"));
        Piloto asignado = cola.asignarDisponible();
        assertEquals("OCUPADO", asignado.getEstado());
    }

    @Test(expected = RuntimeException.class)
    public void asignarDisponible_sinPilotosLibres_lanzaExcepcion() {
        cola.asignarDisponible();
    }

    @Test
    public void liberarPiloto_cambiaALibre() {
        cola.registrar(p("001"));
        Piloto asignado = cola.asignarDisponible();
        cola.liberarPiloto(asignado);
        assertEquals("LIBRE", asignado.getEstado());
        assertEquals(1, cola.contarLibres());
    }

    @Test
    public void cambiarEstado_pilotoExistente_actualizaEstado() {
        cola.registrar(p("001"));
        assertTrue(cola.cambiarEstado("001", "DE_VACACIONES"));
        assertEquals("DE_VACACIONES", cola.buscarPorCui("001").getEstado());
    }

    @Test
    public void contarLibres_soloLosLibres() {
        cola.registrar(p("001"));
        cola.registrar(p("002"));
        cola.asignarDisponible();
        assertEquals(1, cola.contarLibres());
    }

    @Test
    public void estaVacia_colaVacia_retornaTrue() {
        assertTrue(cola.estaVacia());
    }

    @Test
    public void fifo_asignaEnOrdenDeRegistro() {
        cola.registrar(p("001"));
        cola.registrar(p("002"));
        Piloto primero = cola.asignarDisponible();
        assertEquals("001", primero.getCui());
    }
}
