package com.enlatadosmg.service;

import com.enlatadosmg.estructuras.*;
import com.enlatadosmg.model.CatalogoDeProductos;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataStore {

    private final ListaDeUsuarios  listaDeUsuarios  = new ListaDeUsuarios();
    private final PilaDeAlmacen    pilaDeAlmacen    = new PilaDeAlmacen();
    private final ArbolDeClientes  arbolDeClientes  = new ArbolDeClientes();
    private final ColadePilotos    coladePilotos    = new ColadePilotos();
    private final ColaDeVehiculos  colaDeVehiculos  = new ColaDeVehiculos();
    private final ListaDePedidos   listaDePedidos   = new ListaDePedidos();

    private final List<CatalogoDeProductos> catalogoDeProductos = new ArrayList<>();

    private int contadorIdUsuarios  = 1;
    private int correlativoCajas    = 1;
    private int contadorPedidos     = 1;

    public ListaDeUsuarios  getListaDeUsuarios()            { return listaDeUsuarios;  }
    public PilaDeAlmacen    getPilaDeAlmacen()              { return pilaDeAlmacen;    }
    public ArbolDeClientes  getArbolDeClientes()            { return arbolDeClientes;  }
    public ColadePilotos    getColadePilotos()              { return coladePilotos;    }
    public ColaDeVehiculos  getColaDeVehiculos()            { return colaDeVehiculos;  }
    public ListaDePedidos   getListaDePedidos()             { return listaDePedidos;   }
    public List<CatalogoDeProductos> getCatalogoDeProductos() { return catalogoDeProductos; }

    public int siguienteIdUsuario()       { return contadorIdUsuarios++; }
    public int siguienteNumeroPedido()    { return contadorPedidos++; }

    public String siguienteCodigoCaja() {
        return String.format("CAJA-%04d", correlativoCajas++);
    }

    public int siguienteCorrelativoCaja() { return correlativoCajas; }
}
