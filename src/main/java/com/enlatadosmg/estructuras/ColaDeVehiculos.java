package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.Vehiculo;
import java.util.ArrayList;
import java.util.List;

public class ColaDeVehiculos {

    private static class TurnoDeVehiculo {
        Vehiculo        vehiculo;
        TurnoDeVehiculo siguienteTurno;

        TurnoDeVehiculo(Vehiculo vehiculo) {
            this.vehiculo       = vehiculo;
            this.siguienteTurno = null;
        }
    }

    private TurnoDeVehiculo primero;
    private TurnoDeVehiculo ultimo;
    private int totalVehiculos;

    public ColaDeVehiculos() {
        this.primero        = null;
        this.ultimo         = null;
        this.totalVehiculos = 0;
    }

    public void registrar(Vehiculo vehiculo) {
        TurnoDeVehiculo nuevoTurno = new TurnoDeVehiculo(vehiculo);
        if (primero == null) {
            primero = nuevoTurno;
            ultimo  = nuevoTurno;
        } else {
            ultimo.siguienteTurno = nuevoTurno;
            ultimo                = nuevoTurno;
        }
        totalVehiculos++;
    }

    public Vehiculo asignarDisponible() {
        TurnoDeVehiculo actual = primero;
        while (actual != null) {
            if (actual.vehiculo.estaDisponible()) {
                actual.vehiculo.setEstado("OCUPADO");
                return actual.vehiculo;
            }
            actual = actual.siguienteTurno;
        }
        throw new RuntimeException("No hay vehiculos LIBRES disponibles en este momento");
    }

    public void liberarVehiculo(Vehiculo vehiculo) {
        Vehiculo encontrado = buscarPorPlaca(vehiculo.getPlaca());
        if (encontrado != null) {
            encontrado.setEstado("LIBRE");
        }
    }

    public boolean cambiarEstado(String placa, String estado) {
        Vehiculo v = buscarPorPlaca(placa);
        if (v == null) return false;
        v.setEstado(estado);
        return true;
    }

    public Vehiculo buscarPorPlaca(String placa) {
        TurnoDeVehiculo actual = primero;
        while (actual != null) {
            if (placa.equals(actual.vehiculo.getPlaca())) return actual.vehiculo;
            actual = actual.siguienteTurno;
        }
        return null;
    }

    public List<Vehiculo> obtenerTodos() {
        List<Vehiculo> resultado = new ArrayList<>();
        TurnoDeVehiculo actual   = primero;
        while (actual != null) {
            resultado.add(actual.vehiculo);
            actual = actual.siguienteTurno;
        }
        return resultado;
    }

    public int contarLibres() {
        int count = 0;
        TurnoDeVehiculo actual = primero;
        while (actual != null) {
            if (actual.vehiculo.estaDisponible()) count++;
            actual = actual.siguienteTurno;
        }
        return count;
    }

    public int getTotalVehiculos()  { return totalVehiculos; }
    public boolean estaVacia()      { return primero == null; }
}
