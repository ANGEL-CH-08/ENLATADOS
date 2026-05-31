package com.enlatadosmg.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Vehiculo implements Asignable {

    private String placa;
    private String marca;
    private String modelo;
    private String color;
    private int    anio;
    private String tipoTransmision;
    private String estado;
    private int    capacidadMaxCajas;
    private int    cajasOcupadas;

    public Vehiculo(String placa, String marca, String modelo,
                    String color, int anio, String tipoTransmision,
                    int capacidadMaxCajas) {
        this.placa             = placa;
        this.marca             = marca;
        this.modelo            = modelo;
        this.color             = color;
        this.anio              = anio;
        this.tipoTransmision   = tipoTransmision;
        this.capacidadMaxCajas = capacidadMaxCajas;
        this.cajasOcupadas     = 0;
        this.estado            = "LIBRE";
    }

    @Override
    public boolean estaDisponible() { return "LIBRE".equals(this.estado); }

    public int  getEspacioDisponible()  { return capacidadMaxCajas - cajasOcupadas; }
    public void ocuparEspacio(int n)    { this.cajasOcupadas += n; }
    public void liberarEspacio(int n)   { this.cajasOcupadas = Math.max(0, cajasOcupadas - n); }

    public String getEstado() { return estado != null ? estado : "LIBRE"; }

    @Override
    public String toString() {
        return "Vehiculo{placa='" + placa + "', capacidad=" + capacidadMaxCajas
               + ", ocupadas=" + cajasOcupadas + ", estado='" + getEstado() + "'}";
    }
}
