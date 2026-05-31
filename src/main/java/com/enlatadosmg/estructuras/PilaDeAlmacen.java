package com.enlatadosmg.estructuras;

import com.enlatadosmg.model.CajaDeProductos;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PilaDeAlmacen {

    private static class NivelDeCaja {
        CajaDeProductos caja;
        NivelDeCaja     nivelInferior;

        NivelDeCaja(CajaDeProductos caja) {
            this.caja         = caja;
            this.nivelInferior = null;
        }
    }

    private NivelDeCaja tope;
    private int totalRegistros;

    public PilaDeAlmacen() {
        this.tope           = null;
        this.totalRegistros = 0;
    }

    public void apilarOSumar(CajaDeProductos nuevaCaja) {
        NivelDeCaja actual = tope;
        while (actual != null) {
            CajaDeProductos c = actual.caja;
            if (nuevaCaja.getCodigoProducto().equals(c.getCodigoProducto())
                    && nuevaCaja.getZona().equals(c.getZona())
                    && nuevaCaja.getPasillo() == c.getPasillo()
                    && nuevaCaja.getEstante() == c.getEstante()
                    && nuevaCaja.getNivel()   == c.getNivel()
                    && "DISPONIBLE".equals(c.getEstado())) {

                c.setCantidadCajas(c.getCantidadCajas() + nuevaCaja.getCantidadCajas());
                return;
            }
            actual = actual.nivelInferior;
        }

        NivelDeCaja nuevoNivel  = new NivelDeCaja(nuevaCaja);
        nuevoNivel.nivelInferior = tope;
        tope                    = nuevoNivel;
        totalRegistros++;
    }

    public List<CajaDeProductos> retirarCajasPorProducto(String codigoProducto, int cantidadSolicitada) {
        int stockTotal = contarCajasPorProducto(codigoProducto);
        if (stockTotal < cantidadSolicitada) {
            throw new RuntimeException("Stock insuficiente para " + codigoProducto
                + ". Disponibles: " + stockTotal + ", solicitadas: " + cantidadSolicitada);
        }

        List<CajaDeProductos> retiradas    = new ArrayList<>();
        int pendiente = cantidadSolicitada;
        NivelDeCaja actual = tope;
        while (actual != null && pendiente > 0) {
            CajaDeProductos c = actual.caja;
            if (codigoProducto.equals(c.getCodigoProducto()) && "DISPONIBLE".equals(c.getEstado())) {
                int disponiblesEnLote = c.getCantidadCajas();
                if (disponiblesEnLote <= pendiente) {

                    retiradas.add(c);
                    c.setCantidadCajas(0);
                    c.setEstado("AGOTADO");
                    pendiente -= disponiblesEnLote;
                } else {

                    CajaDeProductos parcial = crearCajaParaRetiro(c, pendiente);
                    retiradas.add(parcial);
                    c.setCantidadCajas(disponiblesEnLote - pendiente);
                    pendiente = 0;
                }
            }
            actual = actual.nivelInferior;
        }
        return retiradas;
    }

    private CajaDeProductos crearCajaParaRetiro(CajaDeProductos original, int cantidad) {
        CajaDeProductos retiro = new CajaDeProductos(
            original.getId(), original.getCodigoProducto(), original.getCodigoCaja(),
            original.getProducto(), cantidad, original.getPesoKgPorCaja(),
            original.getZona(), original.getPasillo(), original.getEstante(), original.getNivel()
        );
        retiro.setEstado("RETIRADA");
        return retiro;
    }

    public int contarCajasPorProducto(String codigoProducto) {
        int total = 0;
        NivelDeCaja actual = tope;
        while (actual != null) {
            if (codigoProducto.equals(actual.caja.getCodigoProducto())
                    && "DISPONIBLE".equals(actual.caja.getEstado())) {
                total += actual.caja.getCantidadCajas();
            }
            actual = actual.nivelInferior;
        }
        return total;
    }

    public List<CajaDeProductos> obtenerTodas() {
        List<CajaDeProductos> resultado = new ArrayList<>();
        NivelDeCaja actual = tope;
        while (actual != null) {
            resultado.add(actual.caja);
            actual = actual.nivelInferior;
        }
        return resultado;
    }

    public boolean cambiarEstado(int id, String nuevoEstado) {
        NivelDeCaja actual = tope;
        while (actual != null) {
            if (actual.caja.getId() == id) {
                actual.caja.setEstado(nuevoEstado);
                return true;
            }
            actual = actual.nivelInferior;
        }
        return false;
    }

    public int getTotalRegistros()  { return totalRegistros; }
    public boolean estaVacio()      { return tope == null; }
}
