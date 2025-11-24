package com.eps.agenda.services;

import com.eps.agenda.dtos.AgendarCreateDTO;
import com.eps.agenda.dtos.AgendaResponseDTO;
import com.eps.agenda.dtos.DoctorDTO;
import com.eps.agenda.models.Agenda;
import com.eps.agenda.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String USUARIOS_URL = "http://localhost:8081/usuarios/doctores/";

    // Listar todas las agendas
    public List<Agenda> listar() {
        return agendaRepository.findAll();
    }

    public Optional<Agenda> obtenerPorId(Long id) {
        return agendaRepository.findById(id);
    }

    public Agenda crear(AgendarCreateDTO dto) {
        Agenda agenda = new Agenda();
        agenda.setMedicoId(dto.getMedicoId());
        agenda.setFecha(dto.getFecha());
        agenda.setHoraInicio(dto.getHoraInicio());
        agenda.setHoraFin(dto.getHoraFin());
        agenda.setDisponible(Boolean.TRUE.equals(dto.getDisponible()));
        return agendaRepository.save(agenda);
    }

    public Agenda actualizar(Long id, AgendarCreateDTO dto) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada con ID: " + id));

        agenda.setMedicoId(dto.getMedicoId());
        agenda.setFecha(dto.getFecha());
        agenda.setHoraInicio(dto.getHoraInicio());
        agenda.setHoraFin(dto.getHoraFin());
        agenda.setDisponible(Boolean.TRUE.equals(dto.getDisponible()));

        return agendaRepository.save(agenda);
    }

    public void eliminar(Long id) {
        if (!agendaRepository.existsById(id)) {
            throw new RuntimeException("No se encontró agenda con ID: " + id);
        }
        agendaRepository.deleteById(id);
    }

    // Crear franjas de 1 hora
    public List<Agenda> crearFranjas(AgendarCreateDTO dto) {
        LocalDate fecha = dto.getFecha();
        LocalTime inicio = dto.getHoraInicio();
        LocalTime fin = dto.getHoraFin();

        if (inicio == null || fin == null || fecha == null || dto.getMedicoId() == null) {
            throw new IllegalArgumentException("fecha, horaInicio, horaFin y medicoId son obligatorios");
        }

        if (!inicio.isBefore(fin)) {
            throw new IllegalArgumentException("horaInicio debe ser anterior a horaFin");
        }

        List<Agenda> creadas = new ArrayList<>();
        LocalTime cursor = inicio;

        while (cursor.plusHours(1).compareTo(fin) <= 0) {
            Agenda slot = new Agenda();
            slot.setMedicoId(dto.getMedicoId());
            slot.setFecha(fecha);
            slot.setHoraInicio(cursor);
            slot.setHoraFin(cursor.plusHours(1));
            slot.setDisponible(Boolean.TRUE.equals(dto.getDisponible()));

            Agenda saved = agendaRepository.save(slot);
            creadas.add(saved);
            cursor = cursor.plusHours(1);
        }

        return creadas;
    }

    // Listar agendas con info del doctor
    public List<AgendaResponseDTO> listarConDoctor() {
        List<Agenda> agendas = agendaRepository.findAll();
        List<AgendaResponseDTO> resultado = new ArrayList<>();

        for (Agenda a : agendas) {
            DoctorDTO doctor = null;
            try {
                doctor = restTemplate.getForObject(USUARIOS_URL + a.getMedicoId(), DoctorDTO.class);
            } catch (Exception e) {
                // ignorar si falla la llamada
            }

            AgendaResponseDTO dto = new AgendaResponseDTO();
            dto.setId(a.getId());
            dto.setFecha(a.getFecha());
            dto.setHoraInicio(a.getHoraInicio());
            dto.setHoraFin(a.getHoraFin());
            dto.setDisponible(a.getDisponible());
            dto.setMedicoId(a.getMedicoId());
            dto.setDoctor(doctor);

            resultado.add(dto);
        }
        return resultado;
    }

    public List<AgendaResponseDTO> listarPorMedico(Long medicoId) {
        return listarConDoctor()
                .stream()
                .filter(a -> a.getMedicoId().equals(medicoId) && Boolean.TRUE.equals(a.getDisponible()))
                .collect(Collectors.toList());
    }

    public List<AgendaResponseDTO> findByDoctorId(Long doctorId) {
        List<Agenda> agendas = agendaRepository.findByMedicoId(doctorId);

        return agendas.stream().map(a -> {
            AgendaResponseDTO dto = new AgendaResponseDTO();
            dto.setId(a.getId());           // ← ahora coincide con frontend
            dto.setMedicoId(a.getMedicoId());
            dto.setFecha(a.getFecha());
            dto.setHoraInicio(a.getHoraInicio());
            dto.setHoraFin(a.getHoraFin());
            dto.setDisponible(a.getDisponible());
            return dto;
        }).collect(Collectors.toList());
    }


    public List<AgendaResponseDTO> listarPorMedicoYFecha(Long doctorId, LocalDate fecha) {
        List<Agenda> agendas = agendaRepository.findByMedicoIdAndFecha(doctorId, fecha);

        return agendas.stream().map(a -> {
            AgendaResponseDTO dto = new AgendaResponseDTO();
            dto.setId(a.getId());
            dto.setMedicoId(a.getMedicoId());
            dto.setFecha(a.getFecha());
            dto.setHoraInicio(a.getHoraInicio());
            dto.setHoraFin(a.getHoraFin());
            dto.setDisponible(a.getDisponible());
            return dto;
        }).collect(Collectors.toList());
    }

    // ======================================
    // NUEVO: BLOQUEAR una agenda cuando se reserva
    // ======================================
    public Agenda bloquearAgenda(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada con ID: " + id));
        agenda.setDisponible(false);
        return agendaRepository.save(agenda);
    }
}
