package com.eps.usuarios.repository;

import com.eps.usuarios.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la gestión de la entidad {@link Paciente}.
 *
 * Extiende {@link JpaRepository} para proporcionar operaciones CRUD completas,
 * así como capacidades de paginación y ordenamiento.
 *
 * Actualmente no define consultas personalizadas, pero está preparado
 * para añadirlas en caso de que se requieran búsquedas específicas
 * (por documento, por usuario, etc.).
 */
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

}
