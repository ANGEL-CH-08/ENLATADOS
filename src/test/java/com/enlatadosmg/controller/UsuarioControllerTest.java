package com.enlatadosmg.controller;

import com.enlatadosmg.model.Usuario;
import com.enlatadosmg.service.UsuarioService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    @Mock  private UsuarioService usuarioService;
    @InjectMocks private UsuarioController controller;

    private Usuario usuario;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario(1, "Ana", "Lopez", "pass1234");
    }

    @Test
    public void registrar_datosValidos_retorna200() {
        when(usuarioService.registrar("Ana", "Lopez", "pass1234")).thenReturn(usuario);
        assertEquals(HttpStatus.OK, controller.registrar(Map.of("nombre", "Ana", "apellidos", "Lopez", "contrasena", "pass1234")).getStatusCode());
    }

    @Test
    public void registrar_serviceError_retorna400() {
        when(usuarioService.registrar(any(), any(), any())).thenThrow(new RuntimeException("El nombre no puede estar vacio."));
        assertEquals(HttpStatus.BAD_REQUEST, controller.registrar(Map.of("nombre", "", "apellidos", "", "contrasena", "")).getStatusCode());
    }

    @Test
    public void login_credencialesCorrectas_retorna200() {
        when(usuarioService.login("Ana", "pass1234")).thenReturn(usuario);
        assertEquals(HttpStatus.OK, controller.login(Map.of("nombre", "Ana", "contrasena", "pass1234")).getStatusCode());
    }

    @Test
    public void login_credencialesIncorrectas_retorna401() {
        when(usuarioService.login(any(), any())).thenThrow(new RuntimeException("Contrasena incorrecta."));
        assertEquals(HttpStatus.UNAUTHORIZED, controller.login(Map.of("nombre", "Ana", "contrasena", "wrong")).getStatusCode());
    }

    @Test
    public void listar_retornaListaConTodos() {
        when(usuarioService.obtenerTodos()).thenReturn(List.of(usuario));
        ResponseEntity<List<Usuario>> res = controller.listar();
        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    public void editar_usuarioExistente_retorna200() {
        when(usuarioService.editar(1, "Maria", null, null)).thenReturn(usuario);
        assertEquals(HttpStatus.OK, controller.editar(1, Map.of("nombre", "Maria")).getStatusCode());
    }

    @Test
    public void editar_idInexistente_retorna400() {
        when(usuarioService.editar(anyInt(), any(), any(), any())).thenThrow(new RuntimeException("No existe ningun usuario con ID 99."));
        assertEquals(HttpStatus.BAD_REQUEST, controller.editar(99, Map.of("nombre", "X")).getStatusCode());
    }

    @Test
    public void eliminar_usuarioExistente_retorna200() {
        when(usuarioService.eliminar(1)).thenReturn(true);
        assertEquals(HttpStatus.OK, controller.eliminar(1).getStatusCode());
    }

    @Test
    public void eliminar_idInexistente_retorna400() {
        when(usuarioService.eliminar(99)).thenThrow(new RuntimeException("No existe ningun usuario con ID 99."));
        assertEquals(HttpStatus.BAD_REQUEST, controller.eliminar(99).getStatusCode());
    }
}
