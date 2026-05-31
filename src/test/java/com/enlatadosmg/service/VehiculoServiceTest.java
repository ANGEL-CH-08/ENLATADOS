package com.enlatadosmg.service;

import com.enlatadosmg.model.Vehiculo;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VehiculoServiceTest {

    private DataStore       dataStore;
    private VehiculoService service;

    @Before
    public void setUp() {
        dataStore = new DataStore();
        service   = new VehiculoService(dataStore);
    }

    private Vehiculo nuevoVehiculo(String placa) {
        return new Vehiculo(placa, "Toyota", "Hilux", "Blanco", 2022, "Manual", 50);
    }

    @Test
    public void registrar_vehiculoValido_quedaEnColaConEstadoLibre() {
        Vehiculo v = service.registrar(nuevoVehiculo("P-001"));
        assertNotNull(v);
        assertEquals("LIBRE", v.getEstado());
        assertEquals(1, service.getCantidad());
    }

    @Test(expected = RuntimeException.class)
    public void registrar_placaDuplicada_lanzaExcepcion() {
        service.registrar(nuevoVehiculo("P-001"));
        service.registrar(nuevoVehiculo("P-001"));
    }

    @Test(expected = RuntimeException.class)
    public void registrar_sinPlaca_lanzaExcepcion() {
        service.registrar(new Vehiculo(null, "Toyota", "Hilux", "Blanco", 2022, "Manual", 50));
    }

    @Test(expected = RuntimeException.class)
    public void registrar_capacidadCero_lanzaExcepcion() {
        service.registrar(new Vehiculo("P-002", "Toyota", "Hilux", "Blanco", 2022, "Manual", 0));
    }

    @Test
    public void asignar_vehiculoLibre_cambiaAOcupado() {
        service.registrar(nuevoVehiculo("P-001"));
        Vehiculo asignado = service.asignar();
        assertEquals("OCUPADO", asignado.getEstado());
        assertEquals(0, service.getCantidadLibres());
    }

    @Test(expected = RuntimeException.class)
    public void asignar_sinVehiculosLibres_lanzaExcepcion() {
        service.asignar();
    }

    @Test
    public void liberar_vehiculoOcupado_vuelveAEstarLibre() {
        service.registrar(nuevoVehiculo("P-001"));
        Vehiculo v = service.asignar();
        service.liberar(v);
        assertEquals("LIBRE", v.getEstado());
        assertEquals(1, service.getCantidadLibres());
    }

    @Test
    public void editar_vehiculoExistente_actualizaColor() {
        service.registrar(nuevoVehiculo("P-001"));
        Vehiculo editado = service.editar("P-001", null, null, "Negro", null, null, null);
        assertEquals("Negro", editado.getColor());
    }

    @Test(expected = RuntimeException.class)
    public void editar_placaInexistente_lanzaExcepcion() {
        service.editar("NO-EXISTE", null, null, "Rojo", null, null, null);
    }

    @Test
    public void cambiarEstado_vehiculoLibreAFueraServicio_exitoso() {
        service.registrar(nuevoVehiculo("P-001"));
        assertTrue(service.cambiarEstado("P-001", "FUERA_DE_SERVICIO"));
        assertEquals(0, service.getCantidadLibres());
    }

    @Test(expected = RuntimeException.class)
    public void cambiarEstado_vehiculoOcupado_lanzaExcepcion() {
        service.registrar(nuevoVehiculo("P-001"));
        service.asignar();
        service.cambiarEstado("P-001", "LIBRE");
    }
}
