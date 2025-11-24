package com.eps.agenda.repository;

import com.eps.agenda.models.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    List<Agenda> findByMedicoId(Long medicoId);

    List<Agenda> findByMedicoIdAndFecha(Long medicoId, LocalDate fecha);

}
