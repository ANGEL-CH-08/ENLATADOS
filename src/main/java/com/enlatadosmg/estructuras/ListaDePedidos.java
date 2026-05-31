package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.PedidoDeEntrega;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ListaDePedidos {

    private static class EnlaceDePedido {
        PedidoDeEntrega pedido;
        EnlaceDePedido  siguiente;

        EnlaceDePedido(PedidoDeEntrega pedido) {
            this.pedido    = pedido;
            this.siguiente = null;
        }
    }

    private EnlaceDePedido primero;
    private int totalPedidos;

    public ListaDePedidos() {
        this.primero      = null;
        this.totalPedidos = 0;
    }

    public void agregar(PedidoDeEntrega pedido) {
        EnlaceDePedido nuevoEnlace = new EnlaceDePedido(pedido);
        if (primero == null) {
            primero = nuevoEnlace;
        } else {
            EnlaceDePedido actual = primero;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevoEnlace;
        }
        totalPedidos++;
    }

    public PedidoDeEntrega buscar(Predicate<PedidoDeEntrega> condicion) {
        EnlaceDePedido actual = primero;
        while (actual != null) {
            if (condicion.test(actual.pedido)) return actual.pedido;
            actual = actual.siguiente;
        }
        return null;
    }

    public List<PedidoDeEntrega> obtenerTodos() {
        List<PedidoDeEntrega> resultado = new ArrayList<>();
        EnlaceDePedido actual = primero;
        while (actual != null) {
            resultado.add(actual.pedido);
            actual = actual.siguiente;
        }
        return resultado;
    }

    public int getTotalPedidos() { return totalPedidos; }
    public boolean estaVacia()   { return primero == null; }
}
