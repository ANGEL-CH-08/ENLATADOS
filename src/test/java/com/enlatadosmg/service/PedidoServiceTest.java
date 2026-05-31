package com.enlatadosmg.service;

import com.enlatadosmg.model.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class PedidoServiceTest {

    private DataStore       dataStore;
    private AlmacenService  almacenService;
    private PilotoService   pilotoService;
    private VehiculoService vehiculoService;
    private ClienteService  clienteService;
    private CatalogoService catalogoService;
    private PedidoService   service;

    @Before
    public void setUp() {
        dataStore       = new DataStore();
        catalogoService = new CatalogoService(dataStore);
        almacenService  = new AlmacenService(dataStore, catalogoService);
        pilotoService   = new PilotoService(dataStore);
        vehiculoService = new VehiculoService(dataStore);
        clienteService  = new ClienteService(dataStore);
        service         = new PedidoService(dataStore, almacenService, pilotoService,
                                            vehiculoService, clienteService, catalogoService);

        clienteService.agregar(new Cliente("1111", "Maria", "Perez", "111", "Zona 1"));
        pilotoService.registrar(new Piloto("001", "Juan", "Morales", "A", "111"));
        vehiculoService.registrar(new Vehiculo("P-001", "Toyota", "Hilux", "Blanco", 2022, "Manual", 50));
        catalogoService.registrarProducto("PROD-001", "Atun", "desc", 1.5, 24, 50.0);
        almacenService.ingresarCajas("PROD-001", 100, "A", 1, 1, 1);
    }

    private List<Map<String, Object>> lineas(int cantidad) {
        return List.of(Map.of("codigoProducto", "PROD-001", "cantidadCajas", cantidad));
    }

    @Test
    public void crear_pedidoValido_retornaListaConUnPedido() {
        List<PedidoDeEntrega> pedidos = service.crearPedidoMultiProducto(
            "1111", "Guatemala", "Quetzaltenango", lineas(10));
        assertEquals(1, pedidos.size());
        assertEquals("PENDIENTE", pedidos.get(0).getEstado());
        assertEquals(10, pedidos.get(0).getTotalCajas());
    }

    @Test(expected = RuntimeException.class)
    public void crear_clienteInexistente_lanzaExcepcion() {
        service.crearPedidoMultiProducto("9999", "Guatemala", "Quetzaltenango", lineas(10));
    }

    @Test(expected = RuntimeException.class)
    public void crear_stockInsuficiente_lanzaExcepcion() {
        service.crearPedidoMultiProducto("1111", "Guatemala", "Quetzaltenango", lineas(9999));
    }

    @Test(expected = RuntimeException.class)
    public void crear_sinVehiculoLibre_lanzaExcepcion() {
        vehiculoService.asignar();
        service.crearPedidoMultiProducto("1111", "Guatemala", "Quetzaltenango", lineas(5));
    }

    @Test(expected = RuntimeException.class)
    public void crear_sinPilotoLibre_lanzaExcepcion() {
        pilotoService.asignar();
        service.crearPedidoMultiProducto("1111", "Guatemala", "Quetzaltenango", lineas(5));
    }

    @Test
    public void crear_sinPilotoLibre_vehiculoNOquedaOcupado() {
        pilotoService.asignar();
        int libresAntes = vehiculoService.getCantidadLibres();
        try {
            service.crearPedidoMultiProducto("1111", "Guatemala", "Quetzaltenango", lineas(5));
        } catch (RuntimeException ignored) {}
        assertEquals(libresAntes, vehiculoService.getCantidadLibres());
    }

    @Test
    public void completar_pedido_liberaPilotoYVehiculo() {
        List<PedidoDeEntrega> pedidos = service.crearPedidoMultiProducto(
            "1111", "Guatemala", "Quetzaltenango", lineas(10));
        int numeroPedido = pedidos.get(0).getNumeroPedido();
        service.cambiarEstado(numeroPedido, "COMPLETADO", "OK");
        assertEquals(1, pilotoService.getCantidadLibres());
        assertEquals(1, vehiculoService.getCantidadLibres());
    }

    @Test
    public void cambiarEstado_completado_noPermiteModificarDeNuevo() {
        List<PedidoDeEntrega> pedidos = service.crearPedidoMultiProducto(
            "1111", "Guatemala", "Quetzaltenango", lineas(10));
        int num = pedidos.get(0).getNumeroPedido();
        service.cambiarEstado(num, "COMPLETADO", "");
        try {
            service.cambiarEstado(num, "CANCELADO", "");
            fail("Debio lanzar excepcion");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("completado"));
        }
    }

    @Test(expected = RuntimeException.class)
    public void cambiarEstado_pedidoInexistente_lanzaExcepcion() {
        service.cambiarEstado(9999, "COMPLETADO", "");
    }

    @Test
    public void obtenerTodos_despuesDeCrear_retornaListaNoVacia() {
        service.crearPedidoMultiProducto("1111", "Guatemala", "Quetzaltenango", lineas(5));
        assertFalse(service.obtenerTodos().isEmpty());
    }
}
