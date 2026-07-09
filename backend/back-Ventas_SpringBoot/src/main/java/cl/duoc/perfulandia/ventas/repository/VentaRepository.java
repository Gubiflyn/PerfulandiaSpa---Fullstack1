package cl.duoc.perfulandia.ventas.repository;

import cl.duoc.perfulandia.ventas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByUsuarioId(Long usuarioId);
}