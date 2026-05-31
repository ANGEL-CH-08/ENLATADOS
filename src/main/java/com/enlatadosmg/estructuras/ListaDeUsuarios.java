package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ListaDeUsuarios {

    private static class EnlaceDeUsuario {
        Usuario usuario;
        EnlaceDeUsuario siguiente;

        EnlaceDeUsuario(Usuario usuario) {
            this.usuario   = usuario;
            this.siguiente = null;
        }
    }

    private EnlaceDeUsuario primero;

    private int totalUsuarios;

    public ListaDeUsuarios() {
        this.primero       = null;
        this.totalUsuarios = 0;
    }

    public void agregar(Usuario usuario) {
        EnlaceDeUsuario nuevoEnlace = new EnlaceDeUsuario(usuario);
        if (primero == null) {
            primero = nuevoEnlace;
        } else {
            EnlaceDeUsuario actual = primero;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoEnlace;
        }
        totalUsuarios++;
    }

    public Usuario buscar(Predicate<Usuario> condicion) {
        EnlaceDeUsuario actual = primero;
        while (actual != null) {
            if (condicion.test(actual.usuario)) return actual.usuario;
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean eliminar(Predicate<Usuario> condicion) {
        if (primero == null) return false;
        if (condicion.test(primero.usuario)) {
            primero = primero.siguiente;
            totalUsuarios--;
            return true;
        }
        EnlaceDeUsuario actual = primero;
        while (actual.siguiente != null) {
            if (condicion.test(actual.siguiente.usuario)) {
                actual.siguiente = actual.siguiente.siguiente;
                totalUsuarios--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public List<Usuario> obtenerTodos() {
        List<Usuario> resultado = new ArrayList<>();
        EnlaceDeUsuario actual  = primero;
        while (actual != null) {
            resultado.add(actual.usuario);
            actual = actual.siguiente;
        }
        return resultado;
    }

    public int getTotalUsuarios() { return totalUsuarios; }
    public boolean estaVacia()    { return primero == null; }
}
