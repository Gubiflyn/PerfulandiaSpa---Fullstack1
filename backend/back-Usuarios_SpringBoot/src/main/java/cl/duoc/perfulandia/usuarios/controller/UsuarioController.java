package cl.duoc.perfulandia.usuarios.controller;

import cl.duoc.perfulandia.usuarios.dto.LoginRequest;
import cl.duoc.perfulandia.usuarios.dto.LoginResponse;
import cl.duoc.perfulandia.usuarios.dto.RegistroRequest;
import cl.duoc.perfulandia.usuarios.dto.UsuarioResponse;
import cl.duoc.perfulandia.usuarios.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroRequest request) {
        try {
            return ResponseEntity.ok(usuarioService.registrarUsuario(request));
        } catch (RuntimeException error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = usuarioService.iniciarSesion(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }
}