package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ListaDePedidosTest {

    private ListaDePedidos  lista;
    private PedidoDeEntrega pedido;

    @Before
    public void setUp() {
        lista = new ListaDePedidos();
        Cliente  cli = new Cliente("1111", "Maria", "Perez", "111", "Zona 1");
        Piloto   pil = new Piloto("001", "Juan", "Morales", "A", "111");
        Vehiculo veh = new Vehiculo("P-001", "Toyota", "Hilux", "Blanco", 2022, "Manual", 50);
        pedido = new PedidoDeEntrega(1, "Guatemala", "Quetzaltenango", cli, pil, veh);
    }

    @Test
    public void agregar_pedidoNuevo_incrementaTotal() {
        lista.agregar(pedido);
        assertEquals(1, lista.getTotalPedidos());
    }

    @Test
    public void buscar_pedidoExistente_retornaPedido() {
        lista.agregar(pedido);
        PedidoDeEntrega encontrado = lista.buscar(p -> p.getNumeroPedido() == 1);
        assertNotNull(encontrado);
        assertEquals("Quetzaltenango", encontrado.getDepartamentoDestino());
    }

    @Test
    public void buscar_pedidoInexistente_retornaNull() {
        assertNull(lista.buscar(p -> p.getNumeroPedido() == 99));
    }

    @Test
    public void obtenerTodos_retornaEnOrdenDeInsercion() {
        Cliente  cli = new Cliente("2222", "Pedro", "R", "222", "Zona 2");
        Piloto   pil = new Piloto("002", "Rosa",  "L", "B", "222");
        Vehiculo veh = new Vehiculo("P-002", "Chevrolet", "NHR", "Azul", 2020, "Manual", 40);
        PedidoDeEntrega pedido2 = new PedidoDeEntrega(2, "Peten", "Izabal", cli, pil, veh);
        lista.agregar(pedido);
        lista.agregar(pedido2);
        List<PedidoDeEntrega> todos = lista.obtenerTodos();
        assertEquals(2, todos.size());
        assertEquals(1, todos.get(0).getNumeroPedido());
        assertEquals(2, todos.get(1).getNumeroPedido());
    }

    @Test
    public void estaVacia_listaVacia_retornaTrue() {
        assertTrue(lista.estaVacia());
    }

    @Test
    public void pedido_estadoInicial_esPendiente() {
        assertEquals("PENDIENTE", pedido.getEstado());
    }

    @Test
    public void pedido_agregarLinea_actualizaTotalCajasYMonto() {
        pedido.agregarLinea(new LineaDePedido("PROD-001", "Atun", 10, 50.0));
        assertEquals(10, pedido.getTotalCajas());
        assertEquals(500.0, pedido.getTotalPedido(), 0.001);
    }
}
