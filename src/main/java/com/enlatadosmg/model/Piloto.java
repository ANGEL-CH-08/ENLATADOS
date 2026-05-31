package com.enlatadosmg.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Piloto extends Persona implements Asignable {

    private String cui;
    private String licencia;
    private String estado;

    public Piloto(String cui, String nombre, String apellidos,
                  String licencia, String telefono) {
        super(nombre, apellidos, telefono);
        this.cui      = cui;
        this.licencia = licencia;
        this.estado   = "LIBRE";
    }

    @Override
    public String obtenerIdentificacion() {
        return "CUI: " + cui + " | Licencia: " + licencia;
    }

    @Override
    public boolean estaDisponible() { return "LIBRE".equals(this.estado); }

    public String getEstado() { return estado != null ? estado : "LIBRE"; }

    @Override
    public String toString() {
        return "Piloto{cui='" + cui + "', nombre='" + obtenerNombreCompleto()
               + "', licencia='" + licencia + "', estado='" + getEstado() + "'}";
    }
}
