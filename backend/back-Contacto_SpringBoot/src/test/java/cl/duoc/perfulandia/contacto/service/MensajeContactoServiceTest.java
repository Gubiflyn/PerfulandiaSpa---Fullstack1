package cl.duoc.perfulandia.contacto.service;

import cl.duoc.perfulandia.contacto.model.MensajeContacto;
import cl.duoc.perfulandia.contacto.repository.MensajeContactoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MensajeContactoServiceTest {

    @Mock
    private MensajeContactoRepository mensajeContactoRepository;

    @InjectMocks
    private MensajeContactoService mensajeContactoService;

    @Test
    void listarMensajes_retornaMensajesGuardados() {
        MensajeContacto mensaje = new MensajeContacto("Felipe", "felipe@test.cl", "Consulta", "Hola");

        when(mensajeContactoRepository.findAll()).thenReturn(List.of(mensaje));

        List<MensajeContacto> resultado = mensajeContactoService.listarMensajes();

        assertEquals(1, resultado.size());
        assertEquals("Felipe", resultado.get(0).getNombre());
        verify(mensajeContactoRepository).findAll();
    }

    @Test
    void guardarMensaje_valido_asignaEstadoRecibido() {
        MensajeContacto mensaje = new MensajeContacto("Felipe", "felipe@test.cl", "Consulta", "Necesito ayuda");
        mensaje.setEstado(null);

        when(mensajeContactoRepository.save(any(MensajeContacto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MensajeContacto resultado = mensajeContactoService.guardarMensaje(mensaje);

        assertEquals("RECIBIDO", resultado.getEstado());
        verify(mensajeContactoRepository).save(mensaje);
    }

    @Test
    void guardarMensaje_sinEmail_lanzaExcepcion() {
        MensajeContacto mensaje = new MensajeContacto("Felipe", "", "Consulta", "Necesito ayuda");

        RuntimeException error = assertThrows(RuntimeException.class, () -> mensajeContactoService.guardarMensaje(mensaje));

        assertEquals("El email es obligatorio", error.getMessage());
        verify(mensajeContactoRepository, never()).save(any(MensajeContacto.class));
    }

    @Test
    void buscarPorId_cuandoNoExiste_lanzaExcepcion() {
        when(mensajeContactoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(RuntimeException.class, () -> mensajeContactoService.buscarPorId(99L));

        assertTrue(error.getMessage().contains("Mensaje no encontrado"));
    }

    @Test
    void listarPorEstado_usaRepositorio() {
        MensajeContacto mensaje = new MensajeContacto("Felipe", "felipe@test.cl", "Consulta", "Hola");

        when(mensajeContactoRepository.findByEstado("RECIBIDO")).thenReturn(List.of(mensaje));

        List<MensajeContacto> resultado = mensajeContactoService.listarPorEstado("RECIBIDO");

        assertEquals(1, resultado.size());
        verify(mensajeContactoRepository).findByEstado("RECIBIDO");
    }

    @Test
    void marcarComoRevisado_cambiaEstado() {
        MensajeContacto mensaje = new MensajeContacto("Felipe", "felipe@test.cl", "Consulta", "Hola");
        mensaje.setId(1L);

        when(mensajeContactoRepository.findById(1L)).thenReturn(Optional.of(mensaje));
        when(mensajeContactoRepository.save(any(MensajeContacto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MensajeContacto resultado = mensajeContactoService.marcarComoRevisado(1L);

        assertEquals("REVISADO", resultado.getEstado());
        verify(mensajeContactoRepository).save(mensaje);
    }
}