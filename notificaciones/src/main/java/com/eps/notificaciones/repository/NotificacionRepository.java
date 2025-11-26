package com.eps.notificaciones.repository;

import com.eps.notificaciones.model.Notificacion;
import com.eps.notificaciones.model.NotificacionTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Notificacion}.
 *
 * Proporciona métodos para acceder, consultar y filtrar notificaciones
 * en la base de datos de acuerdo con diferentes criterios como el ID del paciente,
 * el ID de la cita, el tipo de notificación o el estado del envío.
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    /**
     * Obtiene todas las notificaciones asociadas a un paciente específico.
     *
     * @param pacienteId ID del paciente.
     * @return lista de notificaciones relacionadas con ese paciente.
     */
    List<Notificacion> findByPacienteId(Long pacienteId);

    /**
     * Obtiene todas las notificaciones asociadas a una cita específica.
     *
     * @param citaId ID de la cita.
     * @return lista de notificaciones asociadas a esa cita.
     */
    List<Notificacion> findByCitaId(Long citaId);

    /**
     * Obtiene todas las notificaciones filtradas por un tipo específico.
     *
     * @param tipo tipo de notificación.
     * @return lista de notificaciones coincidentes.
     */
    List<Notificacion> findByTipo(NotificacionTipo tipo);

    /**
     * Obtiene todas las notificaciones filtradas por estado.
     * El estado puede ser valores como: "ENVIADO", "FALLIDO", etc.
     *
     * @param estado estado de la notificación.
     * @return lista de notificaciones que coinciden con el estado.
     */
    List<Notificacion> findByEstado(String estado);
}
