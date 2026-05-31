package com.enlatadosmg.service;

import com.enlatadosmg.model.Piloto;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PilotoServiceTest {

    private DataStore     dataStore;
    private PilotoService service;

    @Before
    public void setUp() {
        dataStore = new DataStore();
        service   = new PilotoService(dataStore);
    }

    private Piloto nuevoPiloto(String cui) {
        return new Piloto(cui, "Juan", "Morales", "A", "55551234");
    }

    @Test
    public void registrar_pilotoValido_quedaEnColaConEstadoLibre() {
        Piloto p = service.registrar(nuevoPiloto("001"));
        assertNotNull(p);
        assertEquals("LIBRE", p.getEstado());
        assertEquals(1, service.getCantidad());
    }

    @Test(expected = RuntimeException.class)
    public void registrar_cuiDuplicado_lanzaExcepcion() {
        service.registrar(nuevoPiloto("001"));
        service.registrar(nuevoPiloto("001"));
    }

    @Test(expected = RuntimeException.class)
    public void registrar_sinCui_lanzaExcepcion() {
        service.registrar(new Piloto(null, "Juan", "Morales", "A", "111"));
    }

    @Test
    public void asignar_pilotoLibre_cambiaAOcupadoYRetorna() {
        service.registrar(nuevoPiloto("001"));
        Piloto asignado = service.asignar();
        assertEquals("OCUPADO", asignado.getEstado());
        assertEquals(0, service.getCantidadLibres());
    }

    @Test(expected = RuntimeException.class)
    public void asignar_sinPilotosLibres_lanzaExcepcion() {
        service.asignar();
    }

    @Test
    public void liberar_pilotoOcupado_vuelveAEstarLibre() {
        service.registrar(nuevoPiloto("001"));
        Piloto p = service.asignar();
        service.liberar(p);
        assertEquals("LIBRE", p.getEstado());
        assertEquals(1, service.getCantidadLibres());
    }

    @Test
    public void editar_pilotoExistente_actualizaNombre() {
        service.registrar(nuevoPiloto("001"));
        Piloto editado = service.editar("001", "Carlos", null, null, null);
        assertEquals("Carlos", editado.getNombre());
    }

    @Test(expected = RuntimeException.class)
    public void editar_cuiInexistente_lanzaExcepcion() {
        service.editar("999", "X", null, null, null);
    }

    @Test
    public void cambiarEstado_pilotoLibreAVacaciones_exitoso() {
        service.registrar(nuevoPiloto("001"));
        assertTrue(service.cambiarEstado("001", "DE_VACACIONES"));
        assertEquals(0, service.getCantidadLibres());
    }

    @Test(expected = RuntimeException.class)
    public void cambiarEstado_pilotoOcupado_lanzaExcepcion() {
        service.registrar(nuevoPiloto("001"));
        service.asignar();
        service.cambiarEstado("001", "LIBRE");
    }
}
