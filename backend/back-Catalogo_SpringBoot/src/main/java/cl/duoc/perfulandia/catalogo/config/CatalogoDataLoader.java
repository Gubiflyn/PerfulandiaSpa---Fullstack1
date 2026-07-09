package cl.duoc.perfulandia.catalogo.config;

import cl.duoc.perfulandia.catalogo.model.Perfume;
import cl.duoc.perfulandia.catalogo.repository.PerfumeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CatalogoDataLoader implements CommandLineRunner {

    private final PerfumeRepository perfumeRepository;

    public CatalogoDataLoader(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }

    @Override
    public void run(String... args) {
        if (perfumeRepository.count() == 0) {
            perfumeRepository.save(new Perfume(
                    "Sauvage",
                    "Dior",
                    100,
                    89990,
                    10,
                    "https://images.unsplash.com/photo-1541643600914-78b084683601"
            ));

            perfumeRepository.save(new Perfume(
                    "Bleu de Chanel",
                    "Chanel",
                    100,
                    94990,
                    8,
                    "https://images.unsplash.com/photo-1592945403244-b3fbafd7f539"
            ));

            perfumeRepository.save(new Perfume(
                    "Good Girl",
                    "Carolina Herrera",
                    80,
                    79990,
                    12,
                    "https://images.unsplash.com/photo-1585386959984-a41552231658"
            ));

            perfumeRepository.save(new Perfume(
                    "Light Blue",
                    "Dolce & Gabbana",
                    100,
                    69990,
                    15,
                    "https://images.unsplash.com/photo-1590736704728-f4730bb30770"
            ));
        }
    }
}