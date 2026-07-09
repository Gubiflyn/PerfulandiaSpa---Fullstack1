package cl.duoc.perfulandia.contacto.controller;

import cl.duoc.perfulandia.contacto.model.MensajeContacto;
import cl.duoc.perfulandia.contacto.service.MensajeContactoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contactos")
@CrossOrigin(origins = "*")
public class MensajeContactoController {

    private final MensajeContactoService mensajeContactoService;

    public MensajeContactoController(MensajeContactoService mensajeContactoService) {
        this.mensajeContactoService = mensajeContactoService;
    }

    @GetMapping
    public ResponseEntity<List<MensajeContacto>> listarMensajes() {
        return ResponseEntity.ok(mensajeContactoService.listarMensajes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensajeContacto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mensajeContactoService.buscarPorId(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<MensajeContacto>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(mensajeContactoService.listarPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<?> guardarMensaje(@RequestBody MensajeContacto mensajeContacto) {
        try {
            return ResponseEntity.ok(mensajeContactoService.guardarMensaje(mensajeContacto));
        } catch (RuntimeException error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @PutMapping("/{id}/revisado")
    public ResponseEntity<MensajeContacto> marcarComoRevisado(@PathVariable Long id) {
        return ResponseEntity.ok(mensajeContactoService.marcarComoRevisado(id));
    }
}