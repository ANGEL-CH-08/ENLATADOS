package com.enlatadosmg.service;

import com.enlatadosmg.model.Usuario;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class UsuarioServiceTest {

    private DataStore    dataStore;
    private UsuarioService service;

    @Before
    public void setUp() {
        dataStore = new DataStore();
        service   = new UsuarioService(dataStore);
    }

    @Test
    public void registrar_datosValidos_retornaUsuarioConId() {
        Usuario u = service.registrar("Ana", "Lopez", "pass1234");
        assertNotNull(u);
        assertTrue(u.getId() > 0);
        assertEquals("Ana", u.getNombre());
    }

    @Test(expected = RuntimeException.class)
    public void registrar_nombreVacio_lanzaExcepcion() {
        service.registrar("", "Lopez", "pass1234");
    }

    @Test(expected = RuntimeException.class)
    public void registrar_contrasenaMenorA4_lanzaExcepcion() {
        service.registrar("Ana", "Lopez", "abc");
    }

    @Test
    public void login_credencialesCorrectas_retornaUsuario() {
        service.registrar("Ana", "Lopez", "pass1234");
        Usuario logueado = service.login("Ana", "pass1234");
        assertNotNull(logueado);
        assertEquals("Ana", logueado.getNombre());
    }

    @Test(expected = RuntimeException.class)
    public void login_usuarioNoExiste_lanzaExcepcion() {
        service.login("Nadie", "pass1234");
    }

    @Test(expected = RuntimeException.class)
    public void login_contrasenaIncorrecta_lanzaExcepcion() {
        service.registrar("Ana", "Lopez", "pass1234");
        service.login("Ana", "wrongpass");
    }

    @Test
    public void editar_nombreNuevo_actualizaUsuario() {
        Usuario u = service.registrar("Ana", "Lopez", "pass1234");
        Usuario editado = service.editar(u.getId(), "Maria", null, null);
        assertEquals("Maria", editado.getNombre());
    }

    @Test(expected = RuntimeException.class)
    public void editar_idInexistente_lanzaExcepcion() {
        service.editar(999, "X", null, null);
    }

    @Test
    public void eliminar_usuarioExistente_loRemueveDeLaLista() {
        Usuario u = service.registrar("Ana", "Lopez", "pass1234");
        assertTrue(service.eliminar(u.getId()));
        List<Usuario> todos = service.obtenerTodos();
        assertTrue(todos.stream().noneMatch(x -> x.getId() == u.getId()));
    }

    @Test(expected = RuntimeException.class)
    public void eliminar_idInexistente_lanzaExcepcion() {
        service.eliminar(999);
    }

    @Test
    public void obtenerTodos_variosUsuarios_retornaListaCompleta() {
        service.registrar("Ana",  "Lopez", "pass1111");
        service.registrar("Luis", "Perez", "pass2222");
        assertEquals(2, service.obtenerTodos().size());
    }
}
