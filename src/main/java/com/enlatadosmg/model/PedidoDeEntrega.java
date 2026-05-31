package com.enlatadosmg.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PedidoDeEntrega {

    private int                   numeroPedido;
    private String                departamentoOrigen;
    private String                departamentoDestino;
    private LocalDateTime         fechaHoraInicio;
    private Cliente               cliente;
    private Piloto                piloto;
    private Vehiculo              vehiculo;
    private List<LineaDePedido>   lineas;
    private List<CajaDeProductos> cajasAsignadas;
    private int                   totalCajas;
    private double                totalPedido;
    private String                estado;
    private String                observaciones;
    private int                   numeroCajas;

    public PedidoDeEntrega(int numeroPedido, String departamentoOrigen,
                           String departamentoDestino, Cliente cliente,
                           Piloto piloto, Vehiculo vehiculo) {
        this.numeroPedido        = numeroPedido;
        this.departamentoOrigen  = departamentoOrigen;
        this.departamentoDestino = departamentoDestino;
        this.fechaHoraInicio     = LocalDateTime.now();
        this.cliente             = cliente;
        this.piloto              = piloto;
        this.vehiculo            = vehiculo;
        this.lineas              = new ArrayList<>();
        this.cajasAsignadas      = new ArrayList<>();
        this.totalCajas          = 0;
        this.totalPedido         = 0.0;
        this.estado              = "PENDIENTE";
        this.observaciones       = "";
    }

    public void agregarLinea(LineaDePedido linea) {
        this.lineas.add(linea);
        this.totalCajas  += linea.getCantidadCajas();
        this.totalPedido += linea.getSubtotal();
    }

    public void agregarCajas(List<CajaDeProductos> cajas) {
        if (cajas != null) this.cajasAsignadas.addAll(cajas);
    }

    @Override
    public String toString() {
        return "PedidoDeEntrega{num=" + numeroPedido + ", destino='" + departamentoDestino
               + "', cajas=" + totalCajas + ", total=Q" + totalPedido + ", estado='" + estado + "'}";
    }
}
