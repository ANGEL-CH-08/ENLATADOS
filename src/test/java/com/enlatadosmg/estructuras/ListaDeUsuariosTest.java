package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.Usuario;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ListaDeUsuariosTest {

    private ListaDeUsuarios lista;

    @Before
    public void setUp() {
        lista = new ListaDeUsuarios();
    }

    @Test
    public void agregar_usuarioNuevo_incrementaTotal() {
        lista.agregar(new Usuario(1, "Ana", "Lopez", "pass"));
        assertEquals(1, lista.getTotalUsuarios());
    }

    @Test
    public void buscar_usuarioExistente_retornaUsuario() {
        lista.agregar(new Usuario(1, "Ana", "Lopez", "pass"));
        Usuario encontrado = lista.buscar(u -> u.getId() == 1);
        assertNotNull(encontrado);
        assertEquals("Ana", encontrado.getNombre());
    }

    @Test
    public void buscar_usuarioInexistente_retornaNull() {
        assertNull(lista.buscar(u -> u.getId() == 99));
    }

    @Test
    public void eliminar_usuarioExistente_decrementaTotal() {
        lista.agregar(new Usuario(1, "Ana", "Lopez", "pass"));
        assertTrue(lista.eliminar(u -> u.getId() == 1));
        assertEquals(0, lista.getTotalUsuarios());
    }

    @Test
    public void eliminar_usuarioInexistente_retornaFalse() {
        assertFalse(lista.eliminar(u -> u.getId() == 99));
    }

    @Test
    public void obtenerTodos_variosUsuarios_retornaEnOrdenDeInsercion() {
        lista.agregar(new Usuario(1, "Ana",  "L", "p1"));
        lista.agregar(new Usuario(2, "Luis", "P", "p2"));
        List<Usuario> todos = lista.obtenerTodos();
        assertEquals(2, todos.size());
        assertEquals(1, todos.get(0).getId());
        assertEquals(2, todos.get(1).getId());
    }

    @Test
    public void estaVacia_listaVacia_retornaTrue() {
        assertTrue(lista.estaVacia());
    }
}
