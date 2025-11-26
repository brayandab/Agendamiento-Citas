package com.eps.citas.repository;

import com.eps.citas.models.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para gestionar operaciones CRUD sobre la entidad {@link Cita}.
 *
 * Esta interfaz extiende {@link JpaRepository}, proporcionando métodos
 * predefinidos para crear, actualizar, eliminar y consultar citas en la
 * base de datos. Además, define consultas derivadas (query methods)
 * para obtener citas según el paciente o médico asignado.
 *
 * Spring Data JPA implementa automáticamente estos métodos basándose
 * en su nombre, sin necesidad de escribir consultas SQL manuales.
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    /**
     * Obtiene todas las citas asociadas a un paciente específico.
     *
     * @param pacienteId ID del paciente.
     * @return Lista de citas registradas para ese paciente.
     */
    List<Cita> findByPacienteId(Long pacienteId);

    /**
     * Obtiene todas las citas asociadas a un médico específico.
     *
     * @param medicoId ID del médico.
     * @return Lista de citas asignadas a ese médico.
     */
    List<Cita> findByMedicoId(Long medicoId);
}

