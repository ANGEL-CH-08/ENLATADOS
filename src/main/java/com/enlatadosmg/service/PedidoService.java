package com.enlatadosmg.service;

import com.enlatadosmg.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PedidoService {

    private final DataStore       dataStore;
    private final AlmacenService  almacenService;
    private final PilotoService   pilotoService;
    private final VehiculoService vehiculoService;
    private final ClienteService  clienteService;
    private final CatalogoService catalogoService;

    public PedidoService(DataStore dataStore, AlmacenService almacenService,
                         PilotoService pilotoService, VehiculoService vehiculoService,
                         ClienteService clienteService, CatalogoService catalogoService) {
        this.dataStore       = dataStore;
        this.almacenService  = almacenService;
        this.pilotoService   = pilotoService;
        this.vehiculoService = vehiculoService;
        this.clienteService  = clienteService;
        this.catalogoService = catalogoService;
    }

    public List<PedidoDeEntrega> crearPedidoMultiProducto(String cuiCliente,
                                                           String deptoOrigen,
                                                           String deptoDestino,
                                                           List<Map<String, Object>> lineas) {

        Cliente cliente = clienteService.buscarPorCui(cuiCliente);
        if (cliente == null)
            throw new RuntimeException("Cliente no encontrado con CUI: " + cuiCliente);

        List<LineaDePedido> lineasValidadas = new ArrayList<>();
        int totalCajasSolicitadas = 0;

        for (Map<String, Object> item : lineas) {
            String codigoProd = item.get("codigoProducto").toString();
            int    cantCajas  = Integer.parseInt(item.get("cantidadCajas").toString());

            CatalogoDeProductos catalogo = catalogoService.buscarPorCodigo(codigoProd);
            if (catalogo == null)
                throw new RuntimeException("Producto no encontrado en catalogo: " + codigoProd);
            if (catalogo.getCajasEnStock() < cantCajas)
                throw new RuntimeException("Stock insuficiente de " + catalogo.getNombreProducto()
                    + ". Disponibles: " + catalogo.getCajasEnStock() + ", solicitadas: " + cantCajas);

            lineasValidadas.add(new LineaDePedido(
                codigoProd, catalogo.getNombreProducto(),
                cantCajas, catalogo.getPrecioUnitario()
            ));
            totalCajasSolicitadas += cantCajas;
        }

        if (buscarCamionCompartible(deptoDestino) == null) {
            if (vehiculoService.getCantidadLibres() == 0)
                throw new RuntimeException("No hay vehiculos LIBRES disponibles. Todos estan ocupados o fuera de servicio.");
            if (pilotoService.getCantidadLibres() == 0)
                throw new RuntimeException("No hay pilotos LIBRES disponibles. Todos estan ocupados o de vacaciones.");
        }

        List<PedidoDeEntrega> pedidosCreados = new ArrayList<>();
        int cajasRestantes = totalCajasSolicitadas;
        int lineaIdx = 0;

        while (cajasRestantes > 0) {

            Vehiculo vehiculo = buscarCamionCompartible(deptoDestino);
            Piloto   piloto;

            if (vehiculo != null) {
                piloto = buscarPilotoDelCamion(vehiculo);
            } else {
                vehiculo = vehiculoService.asignar();
                piloto   = pilotoService.asignar();
            }

            int espacioDisponible    = vehiculo.getEspacioDisponible();
            int cajasEnEstePedido    = Math.min(cajasRestantes, espacioDisponible);

            PedidoDeEntrega pedido = new PedidoDeEntrega(
                dataStore.siguienteNumeroPedido(), deptoOrigen, deptoDestino, cliente, piloto, vehiculo
            );

            int cajasAsignadasEnPedido = 0;
            while (cajasAsignadasEnPedido < cajasEnEstePedido && lineaIdx < lineasValidadas.size()) {
                LineaDePedido linea            = lineasValidadas.get(lineaIdx);
                int           disponibleEnPedido = cajasEnEstePedido - cajasAsignadasEnPedido;
                int           aAsignar           = Math.min(linea.getCantidadCajas(), disponibleEnPedido);

                List<CajaDeProductos> cajasRetiradas =
                    almacenService.retirarCajasPorProducto(linea.getCodigoProducto(), aAsignar);
                pedido.agregarLinea(new LineaDePedido(
                    linea.getCodigoProducto(), linea.getNombreProducto(),
                    aAsignar, linea.getPrecioUnitario()
                ));
                pedido.agregarCajas(cajasRetiradas);
                cajasAsignadasEnPedido += aAsignar;

                if (aAsignar == linea.getCantidadCajas()) {
                    lineaIdx++;
                } else {
                    lineasValidadas.set(lineaIdx, new LineaDePedido(
                        linea.getCodigoProducto(), linea.getNombreProducto(),
                        linea.getCantidadCajas() - aAsignar, linea.getPrecioUnitario()
                    ));
                }
            }

            vehiculo.ocuparEspacio(cajasAsignadasEnPedido);
            vehiculo.setEstado("OCUPADO");

            dataStore.getListaDePedidos().agregar(pedido);
            pedidosCreados.add(pedido);
            cajasRestantes -= cajasAsignadasEnPedido;
        }

        return pedidosCreados;
    }

    private Vehiculo buscarCamionCompartible(String destino) {
        List<PedidoDeEntrega> pedidos = dataStore.getListaDePedidos().obtenerTodos();
        for (PedidoDeEntrega p : pedidos) {
            if ("PENDIENTE".equals(p.getEstado()) || "EN_CAMINO".equals(p.getEstado())) {
                if (destino.equalsIgnoreCase(p.getDepartamentoDestino())) {
                    Vehiculo v = p.getVehiculo();
                    if (v != null && v.getEspacioDisponible() > 0) return v;
                }
            }
        }
        return null;
    }

    private Piloto buscarPilotoDelCamion(Vehiculo vehiculo) {
        List<PedidoDeEntrega> pedidos = dataStore.getListaDePedidos().obtenerTodos();
        for (PedidoDeEntrega p : pedidos) {
            if (p.getVehiculo() != null && p.getVehiculo().getPlaca().equals(vehiculo.getPlaca())) {
                return p.getPiloto();
            }
        }
        return pilotoService.asignar();
    }

    public PedidoDeEntrega cambiarEstado(int numeroPedido, String nuevoEstado, String observaciones) {
        PedidoDeEntrega pedido = dataStore.getListaDePedidos()
            .buscar(p -> p.getNumeroPedido() == numeroPedido);
        if (pedido == null)
            throw new RuntimeException("Pedido no encontrado: " + numeroPedido);

        String actual = pedido.getEstado();
        if ("COMPLETADO".equals(actual) || "CANCELADO".equals(actual))
            throw new RuntimeException("El pedido ya fue " + actual.toLowerCase() + " y no puede modificarse");

        pedido.setEstado(nuevoEstado);
        if (observaciones != null && !observaciones.isBlank())
            pedido.setObservaciones(observaciones);

        if ("COMPLETADO".equals(nuevoEstado) || "CANCELADO".equals(nuevoEstado)) {
            pilotoService.liberar(pedido.getPiloto());
            Vehiculo v = pedido.getVehiculo();
            if (v != null) {
                v.liberarEspacio(pedido.getTotalCajas());
                if (v.getCajasOcupadas() == 0) v.setEstado("LIBRE");
            }
        }
        return pedido;
    }

    public List<PedidoDeEntrega> obtenerTodos()        { return dataStore.getListaDePedidos().obtenerTodos(); }
    public PedidoDeEntrega buscarPorNumero(int numero) { return dataStore.getListaDePedidos().buscar(p -> p.getNumeroPedido() == numero); }
}
