package com.enlatadosmg.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Usuario extends Persona {

    private int    id;
    private String contrasena;

    public Usuario(int id, String nombre, String apellidos, String contrasena) {
        super(nombre, apellidos, null);
        this.id         = id;
        this.contrasena = contrasena;
    }

    @Override
    public String obtenerIdentificacion() { return "ID: " + id; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nombre='" + obtenerNombreCompleto() + "'}";
    }
}
