package com.eps.agenda.services;

import com.eps.agenda.dtos.AgendarCreateDTO;
import com.eps.agenda.dtos.AgendaResponseDTO;
import com.eps.agenda.dtos.DoctorDTO;
import com.eps.agenda.models.Agenda;
import com.eps.agenda.repository.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio encargado de manejar toda la lógica relacionada con la agenda:
 * creación de horarios, validación, bloqueo de franjas y consulta de información
 * con datos adicionales del médico utilizando otro microservicio.
 */
@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String USUARIOS_URL = "http://localhost:8081/usuarios/doctores/";

    /**
     * Devuelve todas las agendas registradas.
     */
    public List<Agenda> listar() {
        return agendaRepository.findAll();
    }

    /**
     * Busca una agenda por ID.
     */
    public Optional<Agenda> obtenerPorId(Long id) {
        return agendaRepository.findById(id);
    }

    /**
     * Crea una agenda individual.
     */
    public Agenda crear(AgendarCreateDTO dto) {
        Agenda agenda = new Agenda();
        agenda.setMedicoId(dto.getMedicoId());
        agenda.setFecha(dto.getFecha());
        agenda.setHoraInicio(dto.getHoraInicio());
        agenda.setHoraFin(dto.getHoraFin());
        agenda.setDisponible(Boolean.TRUE.equals(dto.getDisponible()));
        return agendaRepository.save(agenda);
    }

    /**
     * Actualiza una agenda existente por ID.
     */
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

    /**
     * Elimina una agenda por ID.
     */
    public void eliminar(Long id) {
        if (!agendaRepository.existsById(id)) {
            throw new RuntimeException("No se encontró agenda con ID: " + id);
        }
        agendaRepository.deleteById(id);
    }

    /**
     * Crea varias franjas de 1 hora entre una horaInicio y horaFin.
     * Ejemplo: 8:00–12:00 genera 8-9, 9-10, 10-11, 11-12.
     */
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

        // Generación de franjas de 1 hora
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

    /**
     * Devuelve una lista de agendas incluyendo datos del médico
     * (consumidos desde otro microservicio).
     */
    public List<AgendaResponseDTO> listarConDoctor() {
        List<Agenda> agendas = agendaRepository.findAll();
        List<AgendaResponseDTO> resultado = new ArrayList<>();

        for (Agenda a : agendas) {
            DoctorDTO doctor = null;
            try {
                doctor = restTemplate.getForObject(USUARIOS_URL + a.getMedicoId(), DoctorDTO.class);
            } catch (Exception e) {
                // Si falla el microservicio, simplemente no se agrega la info del doctor
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

    /**
     * Lista solo las agendas disponibles de un médico.
     */
    public List<AgendaResponseDTO> listarPorMedico(Long medicoId) {
        return listarConDoctor()
                .stream()
                .filter(a -> a.getMedicoId().equals(medicoId) && Boolean.TRUE.equals(a.getDisponible()))
                .collect(Collectors.toList());
    }

    /**
     * Lista todas las agendas de un médico sin consultar al microservicio de usuarios.
     */
    public List<AgendaResponseDTO> findByDoctorId(Long doctorId) {
        List<Agenda> agendas = agendaRepository.findByMedicoId(doctorId);

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

    /**
     * Lista las agendas filtrando por médico y fecha.
     */
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

    /**
     * Bloquea una agenda específica por ID usando transacción para evitar conflictos.
     * (Se usa cuando el paciente empieza un proceso de agendamiento).
     */
    @Transactional
    public Agenda bloquearAgenda(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada con ID: " + id));

        if (!Boolean.TRUE.equals(agenda.getDisponible())) {
            throw new RuntimeException("Esta agenda ya está ocupada");
        }

        agenda.setDisponible(false);
        return agendaRepository.save(agenda);
    }

    /**
     * Busca y bloquea una franja (doctor + fecha + hora) aplicando un lock pesimista directo.
     * Este método se usa para evitar que dos usuarios reserven la misma hora simultáneamente.
     */
    @Transactional
    public Agenda buscarYBloquearAgenda(Long medicoId, LocalDate fecha, LocalTime hora) {
        Optional<Agenda> agendaOpt = agendaRepository.findByMedicoIdAndFechaAndHoraInicioWithLock(
                medicoId, fecha, hora
        );

        if (!agendaOpt.isPresent()) {
            throw new RuntimeException("No se encontró el horario seleccionado");
        }

        Agenda agenda = agendaOpt.get();

        if (!Boolean.TRUE.equals(agenda.getDisponible())) {
            throw new RuntimeException("Este horario ya no está disponible");
        }

        agenda.setDisponible(false);
        return agendaRepository.save(agenda);
    }

    /**
     * Marca nuevamente una franja como disponible.
     */
    @Transactional
    public Agenda desbloquearAgenda(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada con ID: " + id));

        agenda.setDisponible(true);
        return agendaRepository.save(agenda);
    }

    /**
     * Busca una agenda puntual sin bloquearla (solo para validación rápida).
     */
    public Optional<Agenda> buscarYValidarDisponibilidad(Long medicoId, LocalDate fecha, LocalTime hora) {
        return agendaRepository.findByMedicoIdAndFechaAndHoraInicio(medicoId, fecha, hora);
    }
}
