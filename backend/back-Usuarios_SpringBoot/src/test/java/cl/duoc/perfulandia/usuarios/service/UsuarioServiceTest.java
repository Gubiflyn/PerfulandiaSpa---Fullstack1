package cl.duoc.perfulandia.usuarios.service;

import cl.duoc.perfulandia.usuarios.dto.LoginRequest;
import cl.duoc.perfulandia.usuarios.dto.LoginResponse;
import cl.duoc.perfulandia.usuarios.dto.RegistroRequest;
import cl.duoc.perfulandia.usuarios.dto.UsuarioResponse;
import cl.duoc.perfulandia.usuarios.model.Usuario;
import cl.duoc.perfulandia.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void listarUsuarios_retornaUsuariosSinContrasena() {
        Usuario usuario = new Usuario("Felipe", "felipe@test.cl", "clave-encriptada");
        usuario.setId(1L);

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponse> resultado = usuarioService.listarUsuarios();

        assertEquals(1, resultado.size());
        assertEquals("Felipe", resultado.get(0).getNombre());
        assertEquals("felipe@test.cl", resultado.get(0).getEmail());
        assertEquals("CLIENTE", resultado.get(0).getRol());
    }

    @Test
    void registrarUsuario_valido_encriptaContrasenaYRetornaResponse() {
        RegistroRequest request = new RegistroRequest();
        request.setNombre("Felipe");
        request.setEmail("felipe@test.cl");
        request.setContrasena("123456");

        when(usuarioRepository.existsByEmail("felipe@test.cl")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("clave-encriptada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L);
            return usuario;
        });

        UsuarioResponse resultado = usuarioService.registrarUsuario(request);

        assertEquals(1L, resultado.getId());
        assertEquals("Felipe", resultado.getNombre());
        assertEquals("felipe@test.cl", resultado.getEmail());
        verify(passwordEncoder).encode("123456");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_emailDuplicado_lanzaExcepcion() {
        RegistroRequest request = new RegistroRequest();
        request.setNombre("Felipe");
        request.setEmail("felipe@test.cl");
        request.setContrasena("123456");

        when(usuarioRepository.existsByEmail("felipe@test.cl")).thenReturn(true);

        RuntimeException error = assertThrows(RuntimeException.class, () -> usuarioService.registrarUsuario(request));

        assertEquals("El email ya está registrado", error.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_sinNombre_lanzaExcepcion() {
        RegistroRequest request = new RegistroRequest();
        request.setNombre(" ");
        request.setEmail("felipe@test.cl");
        request.setContrasena("123456");

        RuntimeException error = assertThrows(RuntimeException.class, () -> usuarioService.registrarUsuario(request));

        assertEquals("El nombre es obligatorio", error.getMessage());
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void iniciarSesion_credencialesCorrectas_retornaLoginAutenticado() {
        Usuario usuario = new Usuario("Felipe", "felipe@test.cl", "clave-encriptada");
        usuario.setId(1L);

        LoginRequest request = new LoginRequest();
        request.setEmail("felipe@test.cl");
        request.setContrasena("123456");

        when(usuarioRepository.findByEmail("felipe@test.cl")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", "clave-encriptada")).thenReturn(true);

        LoginResponse resultado = usuarioService.iniciarSesion(request);

        assertTrue(resultado.getAutenticado());
        assertEquals("Inicio de sesión correcto", resultado.getMensaje());
        assertEquals("Felipe", resultado.getUsuario().getNombre());
    }

    @Test
    void iniciarSesion_contrasenaIncorrecta_lanzaExcepcion() {
        Usuario usuario = new Usuario("Felipe", "felipe@test.cl", "clave-encriptada");

        LoginRequest request = new LoginRequest();
        request.setEmail("felipe@test.cl");
        request.setContrasena("mala");

        when(usuarioRepository.findByEmail("felipe@test.cl")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("mala", "clave-encriptada")).thenReturn(false);

        RuntimeException error = assertThrows(RuntimeException.class, () -> usuarioService.iniciarSesion(request));

        assertEquals("Contraseña incorrecta", error.getMessage());
    }

    @Test
    void iniciarSesion_usuarioInactivo_lanzaExcepcion() {
        Usuario usuario = new Usuario("Felipe", "felipe@test.cl", "clave-encriptada");
        usuario.setActivo(false);

        LoginRequest request = new LoginRequest();
        request.setEmail("felipe@test.cl");
        request.setContrasena("123456");

        when(usuarioRepository.findByEmail("felipe@test.cl")).thenReturn(Optional.of(usuario));

        RuntimeException error = assertThrows(RuntimeException.class, () -> usuarioService.iniciarSesion(request));

        assertEquals("El usuario se encuentra desactivado", error.getMessage());
        verifyNoInteractions(passwordEncoder);
    }
}