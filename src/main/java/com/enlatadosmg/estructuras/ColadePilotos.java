package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.Piloto;
import java.util.ArrayList;
import java.util.List;

public class ColadePilotos {

    private static class TurnoDePiloto {
        Piloto        piloto;
        TurnoDePiloto siguienteTurno;

        TurnoDePiloto(Piloto piloto) {
            this.piloto         = piloto;
            this.siguienteTurno = null;
        }
    }

    private TurnoDePiloto primero;
    private TurnoDePiloto ultimo;
    private int totalPilotos;

    public ColadePilotos() {
        this.primero      = null;
        this.ultimo       = null;
        this.totalPilotos = 0;
    }

    public void registrar(Piloto piloto) {
        TurnoDePiloto nuevoTurno = new TurnoDePiloto(piloto);
        if (primero == null) {
            primero = nuevoTurno;
            ultimo  = nuevoTurno;
        } else {
            ultimo.siguienteTurno = nuevoTurno;
            ultimo                = nuevoTurno;
        }
        totalPilotos++;
    }

    public Piloto asignarDisponible() {
        TurnoDePiloto actual = primero;
        while (actual != null) {
            if (actual.piloto.estaDisponible()) {
                actual.piloto.setEstado("OCUPADO");
                return actual.piloto;
            }
            actual = actual.siguienteTurno;
        }
        throw new RuntimeException("No hay pilotos LIBRES disponibles en este momento");
    }

    public void liberarPiloto(Piloto piloto) {
        Piloto encontrado = buscarPorCui(piloto.getCui());
        if (encontrado != null) {
            encontrado.setEstado("LIBRE");
        }
    }

    public boolean cambiarEstado(String cui, String estado) {
        Piloto p = buscarPorCui(cui);
        if (p == null) return false;
        p.setEstado(estado);
        return true;
    }

    public Piloto buscarPorCui(String cui) {
        TurnoDePiloto actual = primero;
        while (actual != null) {
            if (cui.equals(actual.piloto.getCui())) return actual.piloto;
            actual = actual.siguienteTurno;
        }
        return null;
    }

    public List<Piloto> obtenerTodos() {
        List<Piloto> resultado = new ArrayList<>();
        TurnoDePiloto actual   = primero;
        while (actual != null) {
            resultado.add(actual.piloto);
            actual = actual.siguienteTurno;
        }
        return resultado;
    }

    public int contarLibres() {
        int count = 0;
        TurnoDePiloto actual = primero;
        while (actual != null) {
            if (actual.piloto.estaDisponible()) count++;
            actual = actual.siguienteTurno;
        }
        return count;
    }

    public int getTotalPilotos()    { return totalPilotos; }
    public boolean estaVacia()      { return primero == null; }
}
