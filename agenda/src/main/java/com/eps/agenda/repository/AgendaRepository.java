package com.eps.agenda.repository;

import com.eps.agenda.models.Agenda;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio encargado de manejar las consultas relacionadas con la entidad Agenda.
 * Extiende JpaRepository para obtener operaciones CRUD básicas.
 */
@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    /**
     * Retorna todas las agendas registradas para un médico específico.
     */
    List<Agenda> findByMedicoId(Long medicoId);

    /**
     * Retorna todas las agendas de un médico en una fecha exacta.
     */
    List<Agenda> findByMedicoIdAndFecha(Long medicoId, LocalDate fecha);

    /**
     * Busca una agenda puntual (médico + fecha + hora exacta) aplicando un
     * bloqueo pesimista de escritura para evitar que otros procesos la modifiquen
     * mientras se está usando. Esto evita agendamientos duplicados.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Agenda a WHERE a.medicoId = :medicoId " +
            "AND a.fecha = :fecha AND a.horaInicio = :horaInicio")
    Optional<Agenda> findByMedicoIdAndFechaAndHoraInicioWithLock(
            @Param("medicoId") Long medicoId,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio
    );

    /**
     * Busca una agenda puntual (médico + fecha + hora exacta) sin aplicar ningún tipo de lock.
     * Útil para validaciones rápidas o comprobaciones simples.
     */
    Optional<Agenda> findByMedicoIdAndFechaAndHoraInicio(
            Long medicoId,
            LocalDate fecha,
            LocalTime horaInicio
    );
}