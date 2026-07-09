package cl.duoc.perfulandia.catalogo.repository;

import cl.duoc.perfulandia.catalogo.model.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {

    List<Perfume> findByActivoTrue();

}