package cl.duoc.perfulandia.ventas.controller;

import cl.duoc.perfulandia.ventas.dto.CrearVentaRequest;
import cl.duoc.perfulandia.ventas.model.Venta;
import cl.duoc.perfulandia.ventas.service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Venta>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ventaService.listarPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<?> crearVenta(@RequestBody CrearVentaRequest request) {
        try {
            return ResponseEntity.ok(ventaService.crearVenta(request));
        } catch (RuntimeException error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }
}