package com.eps.agenda.services;

import com.eps.agenda.dtos.AgendarDTO;
import com.eps.agenda.models.Agenda;
import com.eps.agenda.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    public List<Agenda> listar() {
        return agendaRepository.findAll();
    }

    public Optional<Agenda> obtenerPorId(Long id) {
        return agendaRepository.findById(id);
    }

    public Agenda crear(AgendarDTO dto) {
        Agenda agenda = new Agenda();
        agenda.setMedicoId(dto.getMedicoId());
        agenda.setFecha(dto.getFecha());
        agenda.setHoraInicio(dto.getHoraInicio());
        agenda.setHoraFin(dto.getHoraFin());
        agenda.setDisponible(dto.getDisponible());
        return agendaRepository.save(agenda);
    }

    public Agenda actualizar(Long id, AgendarDTO dto) {
        Optional<Agenda> existente = agendaRepository.findById(id);
        if (existente.isEmpty()) {
            throw new RuntimeException("Agenda no encontrada con ID: " + id);
        }

        Agenda agenda = existente.get();
        agenda.setMedicoId(dto.getMedicoId());
        agenda.setFecha(dto.getFecha());
        agenda.setHoraInicio(dto.getHoraInicio());
        agenda.setHoraFin(dto.getHoraFin());
        agenda.setDisponible(dto.getDisponible());
        return agendaRepository.save(agenda);
    }

    public void eliminar(Long id) {
        if (!agendaRepository.existsById(id)) {
            throw new RuntimeException("No se encontró agenda con ID: " + id);
        }
        agendaRepository.deleteById(id);
    }
}
