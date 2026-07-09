package cl.duoc.perfulandia.usuarios.service;

import cl.duoc.perfulandia.usuarios.dto.LoginRequest;
import cl.duoc.perfulandia.usuarios.dto.LoginResponse;
import cl.duoc.perfulandia.usuarios.dto.RegistroRequest;
import cl.duoc.perfulandia.usuarios.dto.UsuarioResponse;
import cl.duoc.perfulandia.usuarios.model.Usuario;
import cl.duoc.perfulandia.usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public UsuarioResponse registrarUsuario(RegistroRequest request) {
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("El email es obligatorio");
        }

        if (request.getContrasena() == null || request.getContrasena().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        String contrasenaEncriptada = passwordEncoder.encode(request.getContrasena());

        Usuario usuario = new Usuario(
                request.getNombre(),
                request.getEmail(),
                contrasenaEncriptada
        );

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return convertirAResponse(usuarioGuardado);
    }

    public LoginResponse iniciarSesion(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("El usuario se encuentra desactivado");
        }

        boolean contrasenaCorrecta = passwordEncoder.matches(
                request.getContrasena(),
                usuario.getContrasena()
        );

        if (!contrasenaCorrecta) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return new LoginResponse(
                true,
                "Inicio de sesión correcto",
                convertirAResponse(usuario)
        );
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        return convertirAResponse(usuario);
    }

    private UsuarioResponse convertirAResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }
}