package com.enlatadosmg.service;

import com.enlatadosmg.model.CajaDeProductos;
import com.enlatadosmg.model.CatalogoDeProductos;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlmacenService {

    private final DataStore      dataStore;
    private final CatalogoService catalogoService;

    public AlmacenService(DataStore dataStore, CatalogoService catalogoService) {
        this.dataStore       = dataStore;
        this.catalogoService = catalogoService;
    }

    public CajaDeProductos ingresarCajas(String codigoProducto, int cantidadCajas,
                                          String zona, int pasillo, int estante, int nivel) {
        CatalogoDeProductos catalogo = catalogoService.buscarPorCodigo(codigoProducto);
        if (catalogo == null) {
            throw new RuntimeException("Producto no encontrado en el catalogo: " + codigoProducto
                + ". Registra primero el producto en 'Registro de Producto Nuevo'.");
        }
        if (cantidadCajas <= 0) {
            throw new RuntimeException("La cantidad de cajas debe ser mayor a 0.");
        }

        String codigoCaja = dataStore.siguienteCodigoCaja();
        int    idLote     = dataStore.siguienteCorrelativoCaja();

        CajaDeProductos lote = new CajaDeProductos(
            idLote, codigoProducto, codigoCaja,
            catalogo.getNombreProducto(), cantidadCajas,
            catalogo.getPesoKgPorCaja(),
            zona.toUpperCase(), pasillo, estante, nivel
        );

        dataStore.getPilaDeAlmacen().apilarOSumar(lote);

        catalogo.sumarCajas(cantidadCajas);
        return lote;
    }

    public List<CajaDeProductos> retirarCajasPorProducto(String codigoProducto, int cantidad) {
        CatalogoDeProductos catalogo = catalogoService.buscarPorCodigo(codigoProducto);
        if (catalogo == null) {
            throw new RuntimeException("Producto no encontrado: " + codigoProducto);
        }
        List<CajaDeProductos> retiradas =
            dataStore.getPilaDeAlmacen().retirarCajasPorProducto(codigoProducto, cantidad);
        catalogo.restarCajas(cantidad);
        return retiradas;
    }

    public boolean cambiarEstadoLote(int id, String nuevoEstado) {
        return dataStore.getPilaDeAlmacen().cambiarEstado(id, nuevoEstado);
    }

    public List<CajaDeProductos> obtenerInventario() {
        return dataStore.getPilaDeAlmacen().obtenerTodas();
    }
}
