package com.enlatadosmg.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CatalogoDeProductos {

    private String codigoProducto;
    private String nombreProducto;
    private String descripcion;
    private double pesoKgPorCaja;
    private int    unidadesPorCaja;
    private double precioUnitario;
    private int    cajasEnStock;

    public CatalogoDeProductos(String codigoProducto, String nombreProducto,
                                String descripcion, double pesoKgPorCaja,
                                int unidadesPorCaja, double precioUnitario) {
        this.codigoProducto  = codigoProducto;
        this.nombreProducto  = nombreProducto;
        this.descripcion     = descripcion;
        this.pesoKgPorCaja   = pesoKgPorCaja;
        this.unidadesPorCaja = unidadesPorCaja;
        this.precioUnitario  = precioUnitario;
        this.cajasEnStock    = 0;
    }

    public void sumarCajas(int cantidad)  { this.cajasEnStock += cantidad; }

    public void restarCajas(int cantidad) {
        if (cantidad > this.cajasEnStock)
            throw new RuntimeException("Stock insuficiente para " + codigoProducto
                + ". Disponibles: " + cajasEnStock + ", solicitadas: " + cantidad);
        this.cajasEnStock -= cantidad;
    }

    @Override
    public String toString() {
        return "CatalogoDeProductos{codigo='" + codigoProducto + "', nombre='" + nombreProducto
               + "', stock=" + cajasEnStock + ", precio=" + precioUnitario + "}";
    }
}
