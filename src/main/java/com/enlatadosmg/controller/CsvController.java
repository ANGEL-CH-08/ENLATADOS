package com.enlatadosmg.controller;

import com.enlatadosmg.service.CsvService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/csv")
public class CsvController {

    private final CsvService csvService;

    public CsvController(CsvService csvService) { this.csvService = csvService; }

    @PostMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> cargarUsuarios(
            @RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) return ResponseEntity.badRequest()
            .body(Map.of("error", "El archivo esta vacio."));
        return ResponseEntity.ok(csvService.cargarUsuarios(archivo));
    }

    @PostMapping("/clientes")
    public ResponseEntity<Map<String, Object>> cargarClientes(
            @RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) return ResponseEntity.badRequest()
            .body(Map.of("error", "El archivo esta vacio."));
        return ResponseEntity.ok(csvService.cargarClientes(archivo));
    }

    @PostMapping("/repartidores")
    public ResponseEntity<Map<String, Object>> cargarRepartidores(
            @RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) return ResponseEntity.badRequest()
            .body(Map.of("error", "El archivo esta vacio."));
        return ResponseEntity.ok(csvService.cargarRepartidores(archivo));
    }

    @PostMapping("/vehiculos")
    public ResponseEntity<Map<String, Object>> cargarVehiculos(
            @RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) return ResponseEntity.badRequest()
            .body(Map.of("error", "El archivo esta vacio."));
        return ResponseEntity.ok(csvService.cargarVehiculos(archivo));
    }

    @PostMapping("/catalogo")
    public ResponseEntity<Map<String, Object>> cargarCatalogo(
            @RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) return ResponseEntity.badRequest()
            .body(Map.of("error", "El archivo esta vacio."));
        return ResponseEntity.ok(csvService.cargarCatalogo(archivo));
    }

    @PostMapping("/productos-almacen")
    public ResponseEntity<Map<String, Object>> cargarProductosAlmacen(
            @RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) return ResponseEntity.badRequest()
            .body(Map.of("error", "El archivo esta vacio."));
        return ResponseEntity.ok(csvService.cargarProductosAlmacen(archivo));
    }
}
