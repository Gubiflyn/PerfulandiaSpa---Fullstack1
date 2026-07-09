package cl.duoc.perfulandia.ventas.service;

import cl.duoc.perfulandia.ventas.dto.CrearVentaRequest;
import cl.duoc.perfulandia.ventas.dto.PerfumeResponse;
import cl.duoc.perfulandia.ventas.model.Venta;
import cl.duoc.perfulandia.ventas.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final RestTemplate restTemplate;

    @Value("${catalogo.service.url}")
    private String catalogoServiceUrl;

    public VentaService(VentaRepository ventaRepository, RestTemplate restTemplate) {
        this.ventaRepository = ventaRepository;
        this.restTemplate = restTemplate;
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public Venta buscarPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    public List<Venta> listarPorUsuario(Long usuarioId) {
        return ventaRepository.findByUsuarioId(usuarioId);
    }

    public Venta crearVenta(CrearVentaRequest request) {
        validarRequest(request);

        PerfumeResponse perfume = obtenerPerfumeDesdeCatalogo(request.getPerfumeId());

        if (perfume.getActivo() == null || !perfume.getActivo()) {
            throw new RuntimeException("El perfume no está disponible");
        }

        if (perfume.getStock() < request.getCantidad()) {
            throw new RuntimeException("Stock insuficiente. Stock disponible: " + perfume.getStock());
        }

        descontarStockEnCatalogo(request.getPerfumeId(), request.getCantidad());

        Integer totalVenta = perfume.getPrecio() * request.getCantidad();

        Venta venta = new Venta(
                request.getUsuarioId(),
                request.getNombreCliente(),
                request.getEmailCliente(),
                perfume.getId(),
                perfume.getNombre(),
                perfume.getMarca(),
                request.getCantidad(),
                perfume.getPrecio(),
                totalVenta
        );

        return ventaRepository.save(venta);
    }

    private void validarRequest(CrearVentaRequest request) {
        if (request.getNombreCliente() == null || request.getNombreCliente().isBlank()) {
            throw new RuntimeException("El nombre del cliente es obligatorio");
        }

        if (request.getEmailCliente() == null || request.getEmailCliente().isBlank()) {
            throw new RuntimeException("El email del cliente es obligatorio");
        }

        if (request.getPerfumeId() == null) {
            throw new RuntimeException("El ID del perfume es obligatorio");
        }

        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }
    }

    private PerfumeResponse obtenerPerfumeDesdeCatalogo(Long perfumeId) {
        String url = catalogoServiceUrl + "/api/perfumes/" + perfumeId;

        PerfumeResponse perfume = restTemplate.getForObject(url, PerfumeResponse.class);

        if (perfume == null) {
            throw new RuntimeException("No se pudo obtener el perfume desde catálogo");
        }

        return perfume;
    }

    private void descontarStockEnCatalogo(Long perfumeId, Integer cantidad) {
        String url = catalogoServiceUrl + "/api/perfumes/" + perfumeId + "/stock/descontar?cantidad=" + cantidad;

        restTemplate.put(url, null);
    }
}