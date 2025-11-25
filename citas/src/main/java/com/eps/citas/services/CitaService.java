package com.eps.citas.services;

import com.eps.citas.dtos.AgendaDTO;
import com.eps.citas.dtos.CitaRequestDTO;
import com.eps.citas.models.Cita;
import com.eps.citas.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String AGENDA_SERVICE_URL = "http://localhost:8082/agenda";

    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    @Transactional
    public Cita guardarCita(CitaRequestDTO dto) {

        // 1. Construir URL para bloquear el horario específico
        String bloquearUrl = String.format(
                "%s/bloquear-horario?medicoId=%d&fecha=%s&hora=%s",
                AGENDA_SERVICE_URL,
                dto.getMedicoId(),
                dto.getFecha().toString(),
                dto.getHora().toString()
        );

        try {
            // 2. Intentar bloquear la agenda (esto valida disponibilidad automáticamente)
            AgendaDTO agendaBloqueada = restTemplate.postForObject(bloquearUrl, null, AgendaDTO.class);

            if (agendaBloqueada == null) {
                throw new RuntimeException("Error al bloquear el horario");
            }

            // 3. Crear la cita solo si el bloqueo fue exitoso
            Cita cita = new Cita();
            cita.setPacienteId(dto.getPacienteId());
            cita.setMedicoId(dto.getMedicoId());
            cita.setEspecialidad(dto.getEspecialidad());
            cita.setFecha(dto.getFecha());
            cita.setHora(dto.getHora());
            cita.setMotivo(dto.getMotivo());
            cita.setEstado("PROGRAMADA");

            // 4. Guardar la cita
            Cita citaGuardada = citaRepository.save(cita);

            return citaGuardada;

        } catch (HttpClientErrorException e) {
            // Manejar errores específicos del servicio de agenda
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new RuntimeException("Este horario ya no está disponible");
            } else if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("No se encontró el horario seleccionado");
            } else {
                throw new RuntimeException("Error al verificar disponibilidad del horario");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al agendar la cita: " + e.getMessage());
        }
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

    @Transactional
    public Cita cancelarCita(Long id) {
        Cita cita = buscarPorId(id);

        if ("CANCELADA".equals(cita.getEstado())) {
            throw new RuntimeException("Esta cita ya está cancelada");
        }

        // Cambiar estado
        cita.setEstado("CANCELADA");
        Cita citaActualizada = citaRepository.save(cita);

        // Intentar desbloquear la agenda (opcional - permite reusar el horario)
        try {
            String desbloquearUrl = String.format(
                    "%s/bloquear-horario?medicoId=%d&fecha=%s&hora=%s",
                    AGENDA_SERVICE_URL,
                    cita.getMedicoId(),
                    cita.getFecha().toString(),
                    cita.getHora().toString()
            );

            // restTemplate.put(desbloquearUrl, null);

        } catch (Exception e) {
            // No fallar si no se puede desbloquear, solo registrar
            System.err.println("No se pudo desbloquear la agenda: " + e.getMessage());
        }

        return citaActualizada;
    }

    public void eliminarPorId(Long id) {
        citaRepository.deleteById(id);
    }
}