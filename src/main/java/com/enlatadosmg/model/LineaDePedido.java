package com.enlatadosmg.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class LineaDePedido {

    private String codigoProducto;
    private String nombreProducto;
    private int    cantidadCajas;
    private double precioUnitario;
    private double subtotal;

    public LineaDePedido(String codigoProducto, String nombreProducto,
                          int cantidadCajas, double precioUnitario) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidadCajas  = cantidadCajas;
        this.precioUnitario = precioUnitario;
        this.subtotal       = cantidadCajas * precioUnitario;
    }

    public void setCantidadCajas(int c)     { this.cantidadCajas = c; recalcular(); }
    public void setPrecioUnitario(double p) { this.precioUnitario = p; recalcular(); }

    private void recalcular() { this.subtotal = cantidadCajas * precioUnitario; }

    @Override
    public String toString() {
        return "LineaDePedido{producto='" + codigoProducto + "', cajas=" + cantidadCajas
               + ", precio=" + precioUnitario + ", subtotal=" + subtotal + "}";
    }
}
