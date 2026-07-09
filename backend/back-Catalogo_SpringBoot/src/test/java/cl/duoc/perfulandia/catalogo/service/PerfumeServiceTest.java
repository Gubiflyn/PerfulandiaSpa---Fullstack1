package cl.duoc.perfulandia.catalogo.service;

import cl.duoc.perfulandia.catalogo.model.Perfume;
import cl.duoc.perfulandia.catalogo.repository.PerfumeRepository;
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
class PerfumeServiceTest {

    @Mock
    private PerfumeRepository perfumeRepository;

    @InjectMocks
    private PerfumeService perfumeService;

    @Test
    void listarPerfumes_retornaPerfumesActivos() {
        Perfume perfume = new Perfume("Sauvage", "Dior", 100, 89990, 10, "imagen.jpg");
        when(perfumeRepository.findByActivoTrue()).thenReturn(List.of(perfume));

        List<Perfume> resultado = perfumeService.listarPerfumes();

        assertEquals(1, resultado.size());
        assertEquals("Sauvage", resultado.get(0).getNombre());
        verify(perfumeRepository).findByActivoTrue();
    }

    @Test
    void guardarPerfume_asignaActivoYStockCuandoVienenNulos() {
        Perfume perfume = new Perfume("Good Girl", "Carolina Herrera", 80, 79990, null, "imagen.jpg");
        perfume.setActivo(null);

        when(perfumeRepository.save(any(Perfume.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Perfume resultado = perfumeService.guardarPerfume(perfume);

        assertTrue(resultado.getActivo());
        assertEquals(0, resultado.getStock());
        verify(perfumeRepository).save(perfume);
    }

    @Test
    void buscarPorId_cuandoExiste_retornaPerfume() {
        Perfume perfume = new Perfume("Bleu", "Chanel", 100, 94990, 8, "imagen.jpg");
        perfume.setId(1L);

        when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));

        Perfume resultado = perfumeService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Bleu", resultado.getNombre());
    }

    @Test
    void buscarPorId_cuandoNoExiste_lanzaExcepcion() {
        when(perfumeRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException error = assertThrows(RuntimeException.class, () -> perfumeService.buscarPorId(99L));

        assertTrue(error.getMessage().contains("Perfume no encontrado"));
    }

    @Test
    void descontarStock_conStockSuficiente_restaCantidad() {
        Perfume perfume = new Perfume("Light Blue", "Dolce & Gabbana", 100, 69990, 15, "imagen.jpg");
        perfume.setId(1L);

        when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));
        when(perfumeRepository.save(any(Perfume.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Perfume resultado = perfumeService.descontarStock(1L, 4);

        assertEquals(11, resultado.getStock());
        verify(perfumeRepository).save(perfume);
    }

    @Test
    void descontarStock_conCantidadInvalida_lanzaExcepcion() {
        RuntimeException error = assertThrows(RuntimeException.class, () -> perfumeService.descontarStock(1L, 0));

        assertEquals("La cantidad debe ser mayor a cero", error.getMessage());
        verifyNoInteractions(perfumeRepository);
    }

    @Test
    void descontarStock_conStockInsuficiente_lanzaExcepcion() {
        Perfume perfume = new Perfume("Sauvage", "Dior", 100, 89990, 2, "imagen.jpg");

        when(perfumeRepository.findById(1L)).thenReturn(Optional.of(perfume));

        RuntimeException error = assertThrows(RuntimeException.class, () -> perfumeService.descontarStock(1L, 5));

        assertTrue(error.getMessage().contains("Stock insuficiente"));
        verify(perfumeRepository, never()).save(any(Perfume.class));
    }
}