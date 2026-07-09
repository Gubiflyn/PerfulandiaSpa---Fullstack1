package cl.duoc.perfulandia.ventas.service;

import cl.duoc.perfulandia.ventas.dto.CrearVentaRequest;
import cl.duoc.perfulandia.ventas.dto.PerfumeResponse;
import cl.duoc.perfulandia.ventas.model.Venta;
import cl.duoc.perfulandia.ventas.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private VentaService ventaService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(ventaService, "catalogoServiceUrl", "http://localhost:8081");
    }

    @Test
    void listarVentas_retornaVentasGuardadas() {
        Venta venta = new Venta(1L, "Felipe", "felipe@test.cl", 1L, "Sauvage", "Dior", 2, 89990, 179980);

        when(ventaRepository.findAll()).thenReturn(List.of(venta));

        List<Venta> resultado = ventaService.listarVentas();

        assertEquals(1, resultado.size());
        assertEquals("Sauvage", resultado.get(0).getNombrePerfume());
        verify(ventaRepository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_retornaVenta() {
        Venta venta = new Venta(1L, "Felipe", "felipe@test.cl", 1L, "Sauvage", "Dior", 2, 89990, 179980);
        venta.setId(1L);

        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Venta resultado = ventaService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals(179980, resultado.getTotalVenta());
    }

    @Test
    void crearVenta_conDatosValidos_descuentaStockYGuardaVenta() {
        CrearVentaRequest request = crearRequestValido();
        PerfumeResponse perfume = crearPerfumeDisponible();

        when(restTemplate.getForObject("http://localhost:8081/api/perfumes/1", PerfumeResponse.class)).thenReturn(perfume);
        when(ventaRepository.save(any(Venta.class))).thenAnswer(invocation -> {
            Venta venta = invocation.getArgument(0);
            venta.setId(10L);
            return venta;
        });

        Venta resultado = ventaService.crearVenta(request);

        assertEquals(10L, resultado.getId());
        assertEquals("Sauvage", resultado.getNombrePerfume());
        assertEquals("Dior", resultado.getMarcaPerfume());
        assertEquals(2, resultado.getCantidad());
        assertEquals(89990, resultado.getPrecioUnitario());
        assertEquals(179980, resultado.getTotalVenta());
        assertEquals("REALIZADA", resultado.getEstado());

        verify(restTemplate).put("http://localhost:8081/api/perfumes/1/stock/descontar?cantidad=2", null);
        verify(ventaRepository).save(any(Venta.class));
    }

    @Test
    void crearVenta_sinNombreCliente_lanzaExcepcionYNoConsultaCatalogo() {
        CrearVentaRequest request = crearRequestValido();
        request.setNombreCliente(" ");

        RuntimeException error = assertThrows(RuntimeException.class, () -> ventaService.crearVenta(request));

        assertEquals("El nombre del cliente es obligatorio", error.getMessage());
        verifyNoInteractions(restTemplate);
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void crearVenta_conStockInsuficiente_lanzaExcepcionYNoGuarda() {
        CrearVentaRequest request = crearRequestValido();
        PerfumeResponse perfume = crearPerfumeDisponible();
        perfume.setStock(1);

        when(restTemplate.getForObject("http://localhost:8081/api/perfumes/1", PerfumeResponse.class)).thenReturn(perfume);

        RuntimeException error = assertThrows(RuntimeException.class, () -> ventaService.crearVenta(request));

        assertTrue(error.getMessage().contains("Stock insuficiente"));
        verify(restTemplate, never()).put(anyString(), any());
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void crearVenta_conPerfumeInactivo_lanzaExcepcionYNoGuarda() {
        CrearVentaRequest request = crearRequestValido();
        PerfumeResponse perfume = crearPerfumeDisponible();
        perfume.setActivo(false);

        when(restTemplate.getForObject("http://localhost:8081/api/perfumes/1", PerfumeResponse.class)).thenReturn(perfume);

        RuntimeException error = assertThrows(RuntimeException.class, () -> ventaService.crearVenta(request));

        assertEquals("El perfume no está disponible", error.getMessage());
        verify(restTemplate, never()).put(anyString(), any());
        verify(ventaRepository, never()).save(any(Venta.class));
    }

    @Test
    void listarPorUsuario_usaRepositorio() {
        Venta venta = new Venta(5L, "Felipe", "felipe@test.cl", 1L, "Sauvage", "Dior", 1, 89990, 89990);

        when(ventaRepository.findByUsuarioId(5L)).thenReturn(List.of(venta));

        List<Venta> resultado = ventaService.listarPorUsuario(5L);

        assertEquals(1, resultado.size());
        verify(ventaRepository).findByUsuarioId(5L);
    }

    private CrearVentaRequest crearRequestValido() {
        CrearVentaRequest request = new CrearVentaRequest();
        request.setUsuarioId(1L);
        request.setNombreCliente("Felipe");
        request.setEmailCliente("felipe@test.cl");
        request.setPerfumeId(1L);
        request.setCantidad(2);
        return request;
    }

    private PerfumeResponse crearPerfumeDisponible() {
        PerfumeResponse perfume = new PerfumeResponse();
        perfume.setId(1L);
        perfume.setNombre("Sauvage");
        perfume.setMarca("Dior");
        perfume.setMl(100);
        perfume.setPrecio(89990);
        perfume.setStock(10);
        perfume.setImagenUrl("imagen.jpg");
        perfume.setActivo(true);
        return perfume;
    }
}