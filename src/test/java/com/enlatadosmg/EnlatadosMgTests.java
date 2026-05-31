package com.enlatadosmg;

import com.enlatadosmg.model.*;
import com.enlatadosmg.estructuras.*;
import com.enlatadosmg.service.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnlatadosMgTests {

    private DataStore       dataStore;
    private CatalogoService catalogoService;
    private AlmacenService  almacenService;
    private PilotoService   pilotoService;
    private VehiculoService vehiculoService;
    private ClienteService  clienteService;
    private PedidoService   pedidoService;
    private UsuarioService  usuarioService;

    @Before
    public void setUp() {
        dataStore       = new DataStore();
        catalogoService = new CatalogoService(dataStore);
        almacenService  = new AlmacenService(dataStore, catalogoService);
        pilotoService   = new PilotoService(dataStore);
        vehiculoService = new VehiculoService(dataStore);
        clienteService  = new ClienteService(dataStore);
        pedidoService   = new PedidoService(dataStore, almacenService, pilotoService,
                                             vehiculoService, clienteService, catalogoService);
        usuarioService  = new UsuarioService(dataStore);
    }

    @Test
    public void integracion_crearPedido_flujoCompleto() {
        usuarioService.registrar("Admin", "Sistema", "admin1234");
        clienteService.agregar(new Cliente("C001", "Pedro", "Gomez", "111", "Zona 5"));
        pilotoService.registrar(new Piloto("P001", "Luis", "Ramos", "A", "222"));
        vehiculoService.registrar(new Vehiculo("MGA001", "Toyota", "Hilux", "Blanco", 2022, "Manual", 50));
        catalogoService.registrarProducto("PROD-001", "Atun", "Lata", 1.5, 24, 50.0);
        almacenService.ingresarCajas("PROD-001", 100, "A", 1, 1, 1);

        var pedidos = pedidoService.crearPedidoMultiProducto(
            "C001", "Guatemala", "Quetzaltenango",
            java.util.List.of(java.util.Map.of("codigoProducto", "PROD-001", "cantidadCajas", 10)));

        assertFalse(pedidos.isEmpty());
        assertEquals("PENDIENTE", pedidos.get(0).getEstado());
        assertEquals(0, pilotoService.getCantidadLibres());
        assertEquals(0, vehiculoService.getCantidadLibres());

        pedidoService.cambiarEstado(pedidos.get(0).getNumeroPedido(), "COMPLETADO", "Entregado OK");

        assertEquals(1, pilotoService.getCantidadLibres());
        assertEquals(1, vehiculoService.getCantidadLibres());
    }

    @Test
    public void integracion_estructuras_funcionanIndependientemente() {
        ListaDeUsuarios lista = new ListaDeUsuarios();
        PilaDeAlmacen   pila  = new PilaDeAlmacen();
        ArbolDeClientes arbol = new ArbolDeClientes();
        ColadePilotos   colaP = new ColadePilotos();
        ColaDeVehiculos colaV = new ColaDeVehiculos();
        ListaDePedidos  listaP = new ListaDePedidos();

        lista.agregar(new Usuario(1, "Ana", "L", "pass"));
        pila.apilarOSumar(new CajaDeProductos(1, "P1", "C1", "Atun", 10, 1.0, "A", 1, 1, 1));
        arbol.insertar(new Cliente("1111", "Maria", "P", "111", "Zona 1"));
        colaP.registrar(new Piloto("001", "Juan", "M", "A", "111"));
        colaV.registrar(new Vehiculo("P-001", "Toyota", "Hilux", "Blanco", 2022, "Manual", 50));

        assertEquals(1, lista.getTotalUsuarios());
        assertEquals(10, pila.contarCajasPorProducto("P1"));
        assertNotNull(arbol.buscarPorCui("1111"));
        assertEquals(1, colaP.contarLibres());
        assertEquals(1, colaV.contarLibres());
        assertTrue(listaP.estaVacia());
    }

    @Test
    public void integracion_herencia_superFuncionaEnSubclases() {
        Cliente  c = new Cliente("C1", "Maria", "Perez", "111", "Zona 1");
        Piloto   p = new Piloto("P1", "Juan", "Morales", "A", "222");
        Usuario  u = new Usuario(1, "Ana", "Lopez", "pass");

        assertEquals("CUI: C1", c.obtenerIdentificacion());
        assertEquals("CUI: P1 | Licencia: A", p.obtenerIdentificacion());
        assertEquals("ID: 1", u.obtenerIdentificacion());
        assertTrue(c.obtenerNombreCompleto().contains("Maria"));
        assertTrue(p.estaDisponible());
    }
}
