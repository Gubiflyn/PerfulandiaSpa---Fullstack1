package cl.duoc.perfulandia.catalogo.service;

import cl.duoc.perfulandia.catalogo.model.Perfume;
import cl.duoc.perfulandia.catalogo.repository.PerfumeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;

    public PerfumeService(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }

    public List<Perfume> listarPerfumes() {
        return perfumeRepository.findByActivoTrue();
    }

    public Perfume buscarPorId(Long id) {
        return perfumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfume no encontrado con ID: " + id));
    }

    public Perfume guardarPerfume(Perfume perfume) {
        if (perfume.getActivo() == null) {
            perfume.setActivo(true);
        }

        if (perfume.getStock() == null) {
            perfume.setStock(0);
        }

        return perfumeRepository.save(perfume);
    }

    public Perfume actualizarPerfume(Long id, Perfume datosPerfume) {
        Perfume perfume = buscarPorId(id);

        perfume.setNombre(datosPerfume.getNombre());
        perfume.setMarca(datosPerfume.getMarca());
        perfume.setMl(datosPerfume.getMl());
        perfume.setPrecio(datosPerfume.getPrecio());
        perfume.setStock(datosPerfume.getStock());
        perfume.setImagenUrl(datosPerfume.getImagenUrl());

        return perfumeRepository.save(perfume);
    }

    public void eliminarPerfume(Long id) {
        Perfume perfume = buscarPorId(id);
        perfume.setActivo(false);
        perfumeRepository.save(perfume);
    }

    public Perfume descontarStock(Long id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero");
        }

        Perfume perfume = buscarPorId(id);

        if (perfume.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el perfume: " + perfume.getNombre());
        }

        perfume.setStock(perfume.getStock() - cantidad);
        return perfumeRepository.save(perfume);
    }
}