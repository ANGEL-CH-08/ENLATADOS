package com.enlatadosmg.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CajaDeProductos {

    private int           id;
    private String        codigoCaja;
    private String        codigoProducto;
    private String        producto;
    private int           cantidadCajas;
    private double        pesoKgPorCaja;
    private LocalDateTime fechaIngreso;
    private String        estado;
    private String        zona;
    private int           pasillo;
    private int           estante;
    private int           nivel;

    public CajaDeProductos(int id, String codigoProducto, String codigoCaja,
                            String producto, int cantidadCajas, double pesoKgPorCaja,
                            String zona, int pasillo, int estante, int nivel) {
        this.id             = id;
        this.codigoProducto = codigoProducto;
        this.codigoCaja     = codigoCaja;
        this.producto       = producto;
        this.cantidadCajas  = cantidadCajas;
        this.pesoKgPorCaja  = pesoKgPorCaja;
        this.zona           = zona;
        this.pasillo        = pasillo;
        this.estante        = estante;
        this.nivel          = nivel;
        this.fechaIngreso   = LocalDateTime.now();
        this.estado         = "DISPONIBLE";
    }

    public String obtenerUbicacion() {
        return "Zona " + zona + " - Pasillo " + pasillo
               + " - Estante " + estante + " - Nivel " + nivel;
    }

    @Override
    public String toString() {
        return "CajaDeProductos{id=" + id + ", codigo='" + codigoCaja
               + "', producto='" + codigoProducto + "', cajas=" + cantidadCajas
               + ", ubicacion='" + obtenerUbicacion() + "', estado='" + estado + "'}";
    }
}
