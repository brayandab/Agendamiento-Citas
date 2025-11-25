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

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findByMedicoId(Long medicoId);

    List<Agenda> findByMedicoIdAndFecha(Long medicoId, LocalDate fecha);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Agenda a WHERE a.medicoId = :medicoId " +
            "AND a.fecha = :fecha AND a.horaInicio = :horaInicio")
    Optional<Agenda> findByMedicoIdAndFechaAndHoraInicioWithLock(
            @Param("medicoId") Long medicoId,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio
    );

    Optional<Agenda> findByMedicoIdAndFechaAndHoraInicio(
            Long medicoId,
            LocalDate fecha,
            LocalTime horaInicio
    );
}