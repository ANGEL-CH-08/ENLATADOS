package com.enlatadosmg.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Cliente extends Persona {

    private String cui;
    private String direccion;

    public Cliente(String cui, String nombre, String apellidos,
                   String telefono, String direccion) {
        super(nombre, apellidos, telefono);
        this.cui       = cui;
        this.direccion = direccion;
    }

    @Override
    public String obtenerIdentificacion() { return "CUI: " + cui; }

    @Override
    public String toString() {
        return "Cliente{cui='" + cui + "', nombre='" + obtenerNombreCompleto() + "'}";
    }
}
