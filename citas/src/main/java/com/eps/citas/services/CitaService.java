package com.eps.citas.services;

import com.eps.citas.dtos.CitaRequestDTO;
import com.eps.citas.models.Cita;
import com.eps.citas.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    public Cita guardarCita(CitaRequestDTO dto) {
        Cita cita = new Cita();
        cita.setPacienteId(dto.getPacienteId());
        cita.setMedicoId(dto.getMedicoId());
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setMotivo(dto.getMotivo());
        cita.setEstado("PROGRAMADA");

        return citaRepository.save(cita);
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));
    }

    public List<Cita> buscarPorPacienteId(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }

    public List<Cita> buscarPorMedicoId(Long medicoId) {
        return citaRepository.findByMedicoId(medicoId);
    }

    public Cita cancelarCita(Long id) {
        Cita cita = buscarPorId(id);
        cita.setEstado("CANCELADA");
        return citaRepository.save(cita);
    }

    public void eliminarPorId(Long id) {
        citaRepository.deleteById(id);
    }
}
