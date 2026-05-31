package com.enlatadosmg.service;

import com.enlatadosmg.model.Cliente;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ClienteServiceTest {

    private DataStore      dataStore;
    private ClienteService service;

    @Before
    public void setUp() {
        dataStore = new DataStore();
        service   = new ClienteService(dataStore);
    }

    private Cliente nuevoCliente(String cui) {
        return new Cliente(cui, "Maria", "Perez", "55551234", "Zona 1");
    }

    @Test
    public void agregar_clienteNuevo_seEncuentraEnArbol() {
        Cliente c = service.agregar(nuevoCliente("1111"));
        assertNotNull(c);
        assertNotNull(service.buscarPorCui("1111"));
    }

    @Test(expected = RuntimeException.class)
    public void agregar_cuiDuplicado_lanzaExcepcion() {
        service.agregar(nuevoCliente("1111"));
        service.agregar(nuevoCliente("1111"));
    }

    @Test
    public void buscarPorCui_noExistente_retornaNull() {
        assertNull(service.buscarPorCui("9999"));
    }

    @Test
    public void modificar_actualizaNombreEnArbol() {
        service.agregar(nuevoCliente("1111"));
        Cliente actualizado = new Cliente("1111", "Editada", "Perez", "55551234", "Zona 2");
        service.modificar(actualizado);
        assertEquals("Editada", service.buscarPorCui("1111").getNombre());
    }

    @Test
    public void eliminar_clienteExistente_retornaTrueYDesaparece() {
        service.agregar(nuevoCliente("1111"));
        assertTrue(service.eliminar("1111"));
        assertNull(service.buscarPorCui("1111"));
    }

    @Test
    public void eliminar_clienteInexistente_retornaFalse() {
        assertFalse(service.eliminar("9999"));
    }

    @Test
    public void obtenerTodos_avlRetornaOrdenInOrder() {
        service.agregar(new Cliente("3333", "C", "G", "1", ""));
        service.agregar(new Cliente("1111", "A", "P", "2", ""));
        service.agregar(new Cliente("2222", "B", "R", "3", ""));
        List<Cliente> todos = service.obtenerTodos();
        assertEquals("1111", todos.get(0).getCui());
        assertEquals("2222", todos.get(1).getCui());
        assertEquals("3333", todos.get(2).getCui());
    }
}
