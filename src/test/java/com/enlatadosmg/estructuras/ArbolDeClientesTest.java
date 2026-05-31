package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.Cliente;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ArbolDeClientesTest {

    private ArbolDeClientes arbol;

    @Before
    public void setUp() {
        arbol = new ArbolDeClientes();
    }

    private Cliente c(String cui) {
        return new Cliente(cui, "Nombre", "Apellido", "111", "Zona");
    }

    @Test
    public void insertar_clienteNuevo_seEncuentraPorCui() {
        arbol.insertar(c("1111"));
        assertNotNull(arbol.buscarPorCui("1111"));
    }

    @Test
    public void insertar_cuiDuplicado_actualizaElNodo() {
        arbol.insertar(new Cliente("1111", "Original", "A", "1", "Z"));
        arbol.insertar(new Cliente("1111", "Editado",  "A", "1", "Z"));
        assertEquals("Editado", arbol.buscarPorCui("1111").getNombre());
    }

    @Test
    public void buscar_cuiInexistente_retornaNull() {
        assertNull(arbol.buscarPorCui("9999"));
    }

    @Test
    public void eliminar_clienteExistente_retornaTrueYEliminaDelArbol() {
        arbol.insertar(c("1111"));
        assertTrue(arbol.eliminar("1111"));
        assertNull(arbol.buscarPorCui("1111"));
    }

    @Test
    public void eliminar_cuiInexistente_retornaFalse() {
        assertFalse(arbol.eliminar("9999"));
    }

    @Test
    public void obtenerTodos_retornaOrdenInOrder() {
        arbol.insertar(c("3333"));
        arbol.insertar(c("1111"));
        arbol.insertar(c("2222"));
        List<Cliente> todos = arbol.obtenerTodos();
        assertEquals("1111", todos.get(0).getCui());
        assertEquals("2222", todos.get(1).getCui());
        assertEquals("3333", todos.get(2).getCui());
    }

    @Test
    public void estaVacio_arbolSinNodos_retornaTrue() {
        assertTrue(arbol.estaVacio());
    }

    @Test
    public void arbol_balanceoAvl_manejaMultiplesInserciones() {
        for (int i = 1; i <= 10; i++) arbol.insertar(c(String.valueOf(i)));
        assertEquals(10, arbol.obtenerTodos().size());
    }
}
