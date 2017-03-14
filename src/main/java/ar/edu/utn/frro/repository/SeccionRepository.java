package ar.edu.utn.frro.repository;

import ar.edu.utn.frro.domain.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Seccion entity.
 */
@SuppressWarnings("unused")
public interface SeccionRepository extends JpaRepository<Seccion,Long> {
}
