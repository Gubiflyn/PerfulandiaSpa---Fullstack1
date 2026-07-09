package cl.duoc.perfulandia.contacto.repository;

import cl.duoc.perfulandia.contacto.model.MensajeContacto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Long> {

    List<MensajeContacto> findByEstado(String estado);
}