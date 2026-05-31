package com.enlatadosmg.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Persona implements Identificable {

    protected String nombre;
    protected String apellidos;
    protected String telefono;

    @Override
    public abstract String obtenerIdentificacion();

    public String obtenerNombreCompleto() {
        return (nombre != null ? nombre : "") + " " + (apellidos != null ? apellidos : "");
    }
}
