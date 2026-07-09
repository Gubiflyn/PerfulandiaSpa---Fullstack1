package cl.duoc.perfulandia.contacto.service;

import cl.duoc.perfulandia.contacto.model.MensajeContacto;
import cl.duoc.perfulandia.contacto.repository.MensajeContactoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MensajeContactoService {

    private final MensajeContactoRepository mensajeContactoRepository;

    public MensajeContactoService(MensajeContactoRepository mensajeContactoRepository) {
        this.mensajeContactoRepository = mensajeContactoRepository;
    }

    public List<MensajeContacto> listarMensajes() {
        return mensajeContactoRepository.findAll();
    }

    public MensajeContacto buscarPorId(Long id) {
        return mensajeContactoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con ID: " + id));
    }

    public MensajeContacto guardarMensaje(MensajeContacto mensajeContacto) {
        validarMensaje(mensajeContacto);

        if (mensajeContacto.getEstado() == null || mensajeContacto.getEstado().isBlank()) {
            mensajeContacto.setEstado("RECIBIDO");
        }

        return mensajeContactoRepository.save(mensajeContacto);
    }

    public List<MensajeContacto> listarPorEstado(String estado) {
        return mensajeContactoRepository.findByEstado(estado);
    }

    public MensajeContacto marcarComoRevisado(Long id) {
        MensajeContacto mensaje = buscarPorId(id);
        mensaje.setEstado("REVISADO");
        return mensajeContactoRepository.save(mensaje);
    }

    private void validarMensaje(MensajeContacto mensajeContacto) {
        if (mensajeContacto.getNombre() == null || mensajeContacto.getNombre().isBlank()) {
            throw new RuntimeException("El nombre es obligatorio");
        }

        if (mensajeContacto.getEmail() == null || mensajeContacto.getEmail().isBlank()) {
            throw new RuntimeException("El email es obligatorio");
        }

        if (mensajeContacto.getAsunto() == null || mensajeContacto.getAsunto().isBlank()) {
            throw new RuntimeException("El asunto es obligatorio");
        }

        if (mensajeContacto.getMensaje() == null || mensajeContacto.getMensaje().isBlank()) {
            throw new RuntimeException("El mensaje es obligatorio");
        }
    }
}