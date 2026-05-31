package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ArbolDeClientes {

    private static class RamaDeCliente {
        Cliente      cliente;
        RamaDeCliente ramaIzquierda;
        RamaDeCliente ramaDerecha;
        int           altura;

        RamaDeCliente(Cliente cliente) {
            this.cliente        = cliente;
            this.ramaIzquierda  = null;
            this.ramaDerecha    = null;
            this.altura         = 1;
        }
    }

    private RamaDeCliente raiz;

    public ArbolDeClientes() {
        this.raiz = null;
    }

    private int altura(RamaDeCliente rama) {
        return rama == null ? 0 : rama.altura;
    }

    private int factorBalance(RamaDeCliente rama) {
        return rama == null ? 0 : altura(rama.ramaIzquierda) - altura(rama.ramaDerecha);
    }

    private void actualizarAltura(RamaDeCliente rama) {
        rama.altura = 1 + Math.max(altura(rama.ramaIzquierda), altura(rama.ramaDerecha));
    }

    private RamaDeCliente rotarDerecha(RamaDeCliente ramaDesbalanceada) {
        RamaDeCliente ramaIzquierda = ramaDesbalanceada.ramaIzquierda;
        RamaDeCliente subRama       = ramaIzquierda.ramaDerecha;
        ramaIzquierda.ramaDerecha     = ramaDesbalanceada;
        ramaDesbalanceada.ramaIzquierda = subRama;
        actualizarAltura(ramaDesbalanceada);
        actualizarAltura(ramaIzquierda);
        return ramaIzquierda;
    }

    private RamaDeCliente rotarIzquierda(RamaDeCliente ramaDesbalanceada) {
        RamaDeCliente ramaDerecha = ramaDesbalanceada.ramaDerecha;
        RamaDeCliente subRama     = ramaDerecha.ramaIzquierda;
        ramaDerecha.ramaIzquierda   = ramaDesbalanceada;
        ramaDesbalanceada.ramaDerecha = subRama;
        actualizarAltura(ramaDesbalanceada);
        actualizarAltura(ramaDerecha);
        return ramaDerecha;
    }

    private RamaDeCliente balancear(RamaDeCliente rama) {
        actualizarAltura(rama);
        int balance = factorBalance(rama);
        if (balance > 1  && factorBalance(rama.ramaIzquierda) >= 0) return rotarDerecha(rama);
        if (balance > 1  && factorBalance(rama.ramaIzquierda) <  0) { rama.ramaIzquierda = rotarIzquierda(rama.ramaIzquierda); return rotarDerecha(rama); }
        if (balance < -1 && factorBalance(rama.ramaDerecha)   <= 0) return rotarIzquierda(rama);
        if (balance < -1 && factorBalance(rama.ramaDerecha)   >  0) { rama.ramaDerecha = rotarDerecha(rama.ramaDerecha); return rotarIzquierda(rama); }
        return rama;
    }

    public void insertar(Cliente cliente) {
        raiz = insertar(raiz, cliente);
    }

    private RamaDeCliente insertar(RamaDeCliente rama, Cliente cliente) {
        if (rama == null) return new RamaDeCliente(cliente);
        int comparacion = cliente.getCui().compareTo(rama.cliente.getCui());
        if      (comparacion < 0) rama.ramaIzquierda = insertar(rama.ramaIzquierda, cliente);
        else if (comparacion > 0) rama.ramaDerecha   = insertar(rama.ramaDerecha,   cliente);
        else                      rama.cliente        = cliente;
        return balancear(rama);
    }

    public Cliente buscarPorCui(String cui) {
        return buscarPorCui(raiz, cui);
    }

    private Cliente buscarPorCui(RamaDeCliente rama, String cui) {
        if (rama == null) return null;
        int comparacion = cui.compareTo(rama.cliente.getCui());
        if      (comparacion < 0) return buscarPorCui(rama.ramaIzquierda, cui);
        else if (comparacion > 0) return buscarPorCui(rama.ramaDerecha,   cui);
        else                      return rama.cliente;
    }

    public boolean eliminar(String cui) {
        if (buscarPorCui(cui) == null) return false;
        raiz = eliminar(raiz, cui);
        return true;
    }

    private RamaDeCliente eliminar(RamaDeCliente rama, String cui) {
        if (rama == null) return null;
        int comparacion = cui.compareTo(rama.cliente.getCui());
        if      (comparacion < 0) rama.ramaIzquierda = eliminar(rama.ramaIzquierda, cui);
        else if (comparacion > 0) rama.ramaDerecha   = eliminar(rama.ramaDerecha,   cui);
        else {
            if (rama.ramaIzquierda == null) return rama.ramaDerecha;
            if (rama.ramaDerecha   == null) return rama.ramaIzquierda;
            RamaDeCliente sucesor = ramaMinima(rama.ramaDerecha);
            rama.cliente      = sucesor.cliente;
            rama.ramaDerecha  = eliminar(rama.ramaDerecha, sucesor.cliente.getCui());
        }
        return balancear(rama);
    }

    private RamaDeCliente ramaMinima(RamaDeCliente rama) {
        while (rama.ramaIzquierda != null) rama = rama.ramaIzquierda;
        return rama;
    }

    public List<Cliente> obtenerTodos() {
        List<Cliente> lista = new ArrayList<>();
        inorden(raiz, lista);
        return lista;
    }

    private void inorden(RamaDeCliente rama, List<Cliente> lista) {
        if (rama == null) return;
        inorden(rama.ramaIzquierda, lista);
        lista.add(rama.cliente);
        inorden(rama.ramaDerecha,   lista);
    }

    public boolean estaVacio() { return raiz == null; }
}
