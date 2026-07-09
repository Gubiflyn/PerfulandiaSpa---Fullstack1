package cl.duoc.perfulandia.catalogo.controller;

import cl.duoc.perfulandia.catalogo.model.Perfume;
import cl.duoc.perfulandia.catalogo.service.PerfumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfumes")
@CrossOrigin(origins = "*")
public class PerfumeController {

    private final PerfumeService perfumeService;

    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping
    public ResponseEntity<List<Perfume>> listarPerfumes() {
        return ResponseEntity.ok(perfumeService.listarPerfumes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Perfume> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(perfumeService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Perfume> guardarPerfume(@RequestBody Perfume perfume) {
        return ResponseEntity.ok(perfumeService.guardarPerfume(perfume));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Perfume> actualizarPerfume(
            @PathVariable Long id,
            @RequestBody Perfume perfume
    ) {
        return ResponseEntity.ok(perfumeService.actualizarPerfume(id, perfume));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPerfume(@PathVariable Long id) {
        perfumeService.eliminarPerfume(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/stock/descontar")
    public ResponseEntity<Perfume> descontarStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad
    ) {
        return ResponseEntity.ok(perfumeService.descontarStock(id, cantidad));
    }
}