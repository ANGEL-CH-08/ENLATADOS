package com.enlatadosmg.service;

import com.enlatadosmg.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import java.util.Map;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CsvServiceTest {

    @Mock private UsuarioService  usuarioService;
    @Mock private ClienteService  clienteService;
    @Mock private PilotoService   pilotoService;
    @Mock private VehiculoService vehiculoService;

    private CsvService csvService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        csvService = new CsvService(usuarioService, clienteService, pilotoService, vehiculoService);
    }

    @Test
    public void cargarUsuarios_csvValido_retornaCargadosCorrectamente() throws Exception {
        String csv = "ID;Nombre;Apellido;Contrasena\n1;Ana;Lopez;pass1234\n2;Luis;Perez;clave5678";
        MockMultipartFile archivo = new MockMultipartFile("archivo", "usuarios.csv", "text/csv", csv.getBytes());
        when(usuarioService.registrar(eq("Ana"),  eq("Lopez"), eq("pass1234"))).thenReturn(new Usuario(1, "Ana",  "Lopez", "pass1234"));
        when(usuarioService.registrar(eq("Luis"), eq("Perez"), eq("clave5678"))).thenReturn(new Usuario(2, "Luis", "Perez", "clave5678"));

        Map<String, Object> result = csvService.cargarUsuarios(archivo);
        assertEquals(2, result.get("cargados"));
        assertEquals(0, result.get("errores"));
    }

    @Test
    public void cargarUsuarios_filaConError_cuentaErrores() throws Exception {
        String csv = "1;Ana;Lopez;pass1234\n2;;Perez;clave";
        MockMultipartFile archivo = new MockMultipartFile("archivo", "u.csv", "text/csv", csv.getBytes());
        when(usuarioService.registrar(eq("Ana"), any(), any())).thenReturn(new Usuario(1, "Ana", "Lopez", "pass1234"));
        when(usuarioService.registrar(eq(""),    any(), any())).thenThrow(new RuntimeException("El nombre no puede estar vacio."));

        Map<String, Object> result = csvService.cargarUsuarios(archivo);
        assertEquals(1, result.get("cargados"));
        assertEquals(1, result.get("errores"));
    }

    @Test
    public void cargarClientes_csvValido_retornaCargadosCorrectamente() throws Exception {
        String csv = "DPI;Nombre;Apellido;Telefono\n1234567890101;Maria;Gomez;55551234";
        MockMultipartFile archivo = new MockMultipartFile("archivo", "clientes.csv", "text/csv", csv.getBytes());
        Cliente c = new Cliente("1234567890101", "Maria", "Gomez", "55551234", "");
        when(clienteService.agregar(any())).thenReturn(c);

        Map<String, Object> result = csvService.cargarClientes(archivo);
        assertEquals(1, result.get("cargados"));
        assertEquals(0, result.get("errores"));
    }

    @Test
    public void cargarRepartidores_csvValido_retornaCargadosCorrectamente() throws Exception {
        String csv = "1111111110101;Carlos;Morales;A;55550001";
        MockMultipartFile archivo = new MockMultipartFile("archivo", "pilotos.csv", "text/csv", csv.getBytes());
        Piloto p = new Piloto("1111111110101", "Carlos", "Morales", "A", "55550001");
        when(pilotoService.registrar(any())).thenReturn(p);

        Map<String, Object> result = csvService.cargarRepartidores(archivo);
        assertEquals(1, result.get("cargados"));
        assertEquals(0, result.get("errores"));
    }

    @Test
    public void cargarVehiculos_csvValido_retornaCargadosCorrectamente() throws Exception {
        String csv = "P001ABC;Toyota;Hilux;Blanco;2022;Manual;50";
        MockMultipartFile archivo = new MockMultipartFile("archivo", "vehiculos.csv", "text/csv", csv.getBytes());
        Vehiculo v = new Vehiculo("P001ABC", "Toyota", "Hilux", "Blanco", 2022, "Manual", 50);
        when(vehiculoService.registrar(any())).thenReturn(v);

        Map<String, Object> result = csvService.cargarVehiculos(archivo);
        assertEquals(1, result.get("cargados"));
        assertEquals(0, result.get("errores"));
    }

    @Test
    public void cargarVehiculos_anioInvalido_cuentaComoError() throws Exception {
        String csv = "P001ABC;Toyota;Hilux;Blanco;INVALIDO;Manual;50";
        MockMultipartFile archivo = new MockMultipartFile("archivo", "vehiculos.csv", "text/csv", csv.getBytes());

        Map<String, Object> result = csvService.cargarVehiculos(archivo);
        assertEquals(0, result.get("cargados"));
        assertEquals(1, result.get("errores"));
    }
}
