package com.enlatadosmg.service;

import com.enlatadosmg.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CsvService {

    private final UsuarioService  usuarioService;
    private final ClienteService  clienteService;
    private final PilotoService   pilotoService;
    private final VehiculoService vehiculoService;

    public CsvService(UsuarioService usuarioService, ClienteService clienteService,
                      PilotoService pilotoService, VehiculoService vehiculoService) {
        this.usuarioService  = usuarioService;
        this.clienteService  = clienteService;
        this.pilotoService   = pilotoService;
        this.vehiculoService = vehiculoService;
    }

    public Map<String, Object> cargarUsuarios(MultipartFile archivo) {
        List<String> errores  = new ArrayList<>();
        List<String> cargados = new ArrayList<>();
        int fila = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                fila++;
                linea = linea.trim();
                if (linea.isEmpty() || fila == 1 && linea.toLowerCase().startsWith("id")) continue;
                String[] cols = linea.split(";", -1);
                if (cols.length < 4) { errores.add("Fila " + fila + ": faltan columnas (se esperan 4)."); continue; }
                try {
                    String nombre    = cols[1].trim();
                    String apellidos = cols[2].trim();
                    String contrasena= cols[3].trim();
                    Usuario u = usuarioService.registrar(nombre, apellidos, contrasena);
                    cargados.add("ID " + u.getId() + " — " + u.obtenerNombreCompleto());
                } catch (Exception e) { errores.add("Fila " + fila + ": " + e.getMessage()); }
            }
        } catch (Exception e) { errores.add("Error al leer el archivo: " + e.getMessage()); }
        return resumen(cargados, errores);
    }

    public Map<String, Object> cargarClientes(MultipartFile archivo) {
        List<String> errores  = new ArrayList<>();
        List<String> cargados = new ArrayList<>();
        int fila = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                fila++;
                linea = linea.trim();
                if (linea.isEmpty() || fila == 1 && linea.toLowerCase().startsWith("dpi")) continue;
                String[] cols = linea.split(";", -1);
                if (cols.length < 4) { errores.add("Fila " + fila + ": faltan columnas (se esperan 4)."); continue; }
                try {
                    String dpi      = cols[0].trim();
                    String nombre   = cols[1].trim();
                    String apellido = cols[2].trim();
                    String telefono = cols[3].trim();
                    String direccion= cols.length > 4 ? cols[4].trim() : "";
                    Cliente c = clienteService.agregar(new Cliente(dpi, nombre, apellido, telefono, direccion));
                    cargados.add("CUI " + c.getCui() + " — " + c.obtenerNombreCompleto());
                } catch (Exception e) { errores.add("Fila " + fila + ": " + e.getMessage()); }
            }
        } catch (Exception e) { errores.add("Error al leer el archivo: " + e.getMessage()); }
        return resumen(cargados, errores);
    }

    public Map<String, Object> cargarRepartidores(MultipartFile archivo) {
        List<String> errores  = new ArrayList<>();
        List<String> cargados = new ArrayList<>();
        int fila = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                fila++;
                linea = linea.trim();
                if (linea.isEmpty() || fila == 1 && linea.toLowerCase().startsWith("dpi")) continue;
                String[] cols = linea.split(";", -1);
                if (cols.length < 5) { errores.add("Fila " + fila + ": faltan columnas (se esperan 5)."); continue; }
                try {
                    String dpi      = cols[0].trim();
                    String nombre   = cols[1].trim();
                    String apellido = cols[2].trim();
                    String licencia = cols[3].trim();
                    String telefono = cols[4].trim();
                    Piloto p = pilotoService.registrar(new Piloto(dpi, nombre, apellido, licencia, telefono));
                    cargados.add("CUI " + p.getCui() + " — " + p.obtenerNombreCompleto());
                } catch (Exception e) { errores.add("Fila " + fila + ": " + e.getMessage()); }
            }
        } catch (Exception e) { errores.add("Error al leer el archivo: " + e.getMessage()); }
        return resumen(cargados, errores);
    }

    public Map<String, Object> cargarVehiculos(MultipartFile archivo) {
        List<String> errores  = new ArrayList<>();
        List<String> cargados = new ArrayList<>();
        int fila = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(archivo.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                fila++;
                linea = linea.trim();
                if (linea.isEmpty() || fila == 1 && linea.toLowerCase().startsWith("placa")) continue;
                String[] cols = linea.split(";", -1);
                if (cols.length < 6) { errores.add("Fila " + fila + ": faltan columnas (se esperan 6)."); continue; }
                try {
                    String  placa        = cols[0].trim();
                    String  marca        = cols[1].trim();
                    String  modelo       = cols[2].trim();
                    String  color        = cols[3].trim();
                    int     anio         = Integer.parseInt(cols[4].trim());
                    String  transmision  = cols[5].trim();
                    int     capacidad    = cols.length > 6 ? Integer.parseInt(cols[6].trim()) : 30;
                    Vehiculo v = vehiculoService.registrar(
                        new Vehiculo(placa, marca, modelo, color, anio, transmision, capacidad));
                    cargados.add("Placa " + v.getPlaca() + " — " + v.getMarca() + " " + v.getModelo());
                } catch (NumberFormatException nfe) {
                    errores.add("Fila " + fila + ": el año o capacidad no es un número valido.");
                } catch (Exception e) { errores.add("Fila " + fila + ": " + e.getMessage()); }
            }
        } catch (Exception e) { errores.add("Error al leer el archivo: " + e.getMessage()); }
        return resumen(cargados, errores);
    }

    private Map<String, Object> resumen(List<String> cargados, List<String> errores) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("cargados", cargados.size());
        r.put("errores",  errores.size());
        r.put("detalleCargados", cargados);
        r.put("detalleErrores",  errores);
        return r;
    }
}
