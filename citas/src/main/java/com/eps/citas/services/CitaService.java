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

import java.time.LocalDateTime;
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
    private final String NOTIFICACIONES_URL = "http://localhost:8084/api/notificaciones";

    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    @Transactional
    public Cita guardarCita(CitaRequestDTO dto) {

        String bloquearUrl = String.format(
                "%s/bloquear-horario?medicoId=%d&fecha=%s&hora=%s",
                AGENDA_SERVICE_URL,
                dto.getMedicoId(),
                dto.getFecha().toString(),
                dto.getHora().toString()
        );

        try {
            AgendaDTO agendaBloqueada = restTemplate.postForObject(bloquearUrl, null, AgendaDTO.class);

            if (agendaBloqueada == null) {
                throw new RuntimeException("Error al bloquear el horario");
            }

            Cita cita = new Cita();
            cita.setPacienteId(dto.getPacienteId());
            cita.setMedicoId(dto.getMedicoId());
            cita.setEspecialidad(dto.getEspecialidad());
            cita.setFecha(dto.getFecha());
            cita.setHora(dto.getHora());
            cita.setMotivo(dto.getMotivo());
            cita.setEstado("PROGRAMADA");

            Cita citaGuardada = citaRepository.save(cita);

            // ✅ ENVIAR NOTIFICACIÓN DE AGENDAMIENTO
            enviarNotificacionAgendamiento(citaGuardada, dto);

            return citaGuardada;

        } catch (HttpClientErrorException e) {
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

    /**
     * ✅ Enviar notificación de agendamiento
     */
    private void enviarNotificacionAgendamiento(Cita cita, CitaRequestDTO dto) {
        try {
            Map<String, Object> notificacion = new HashMap<>();
            notificacion.put("to", dto.getEmailPaciente());
            notificacion.put("namePatient", dto.getNombrePaciente());
            notificacion.put("apptDate", cita.getFecha().toString());
            notificacion.put("tipo", "AGENDAMIENTO_CITA");
            notificacion.put("citaId", cita.getId());
            notificacion.put("pacienteId", cita.getPacienteId());
            notificacion.put("especialidad", cita.getEspecialidad());
            notificacion.put("hora", cita.getHora().toString());
            notificacion.put("nombreDoctor", dto.getNombreDoctor());

            restTemplate.postForObject(NOTIFICACIONES_URL + "/enviar", notificacion, Object.class);
            System.out.println("✅ Notificación de agendamiento enviada a: " + dto.getEmailPaciente());
        } catch (Exception e) {
            System.err.println("❌ Error al enviar notificación de agendamiento: " + e.getMessage());
            // No fallar la cita si falla la notificación
        }
    }

    /**
     * ✅ NUEVO: Enviar notificación de cancelación
     */
    private void enviarNotificacionCancelacion(Cita cita, String emailPaciente, String nombrePaciente) {
        try {
            Map<String, Object> notificacion = new HashMap<>();
            notificacion.put("to", emailPaciente);
            notificacion.put("namePatient", nombrePaciente);
            notificacion.put("apptDate", cita.getFecha().toString());
            notificacion.put("tipo", "CANCELACION_CITA");
            notificacion.put("citaId", cita.getId());
            notificacion.put("pacienteId", cita.getPacienteId());
            notificacion.put("especialidad", cita.getEspecialidad());
            notificacion.put("hora", cita.getHora().toString());

            restTemplate.postForObject(NOTIFICACIONES_URL + "/enviar", notificacion, Object.class);
            System.out.println("✅ Notificación de cancelación enviada a: " + emailPaciente);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar notificación de cancelación: " + e.getMessage());
            // No fallar la cancelación si falla la notificación
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

    /**
     * ✅ MEJORADO: Cancelar cita con validación de 12 horas y notificación
     */
    @Transactional
    public Cita cancelarCita(Long id, String emailPaciente, String nombrePaciente) {
        Cita cita = buscarPorId(id);

        // Validar que no esté ya cancelada
        if ("CANCELADA".equals(cita.getEstado())) {
            throw new RuntimeException("Esta cita ya está cancelada");
        }

        // ✅ VALIDAR 12 HORAS DE ANTICIPACIÓN
        LocalDateTime fechaHoraCita = LocalDateTime.of(cita.getFecha(), cita.getHora());
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limiteMinimo = fechaHoraCita.minusHours(12);

        if (ahora.isAfter(limiteMinimo)) {
            throw new RuntimeException("No se puede cancelar la cita. Debe hacerlo con al menos 12 horas de anticipación");
        }

        // Cambiar estado
        cita.setEstado("CANCELADA");
        Cita citaActualizada = citaRepository.save(cita);

        // ✅ ENVIAR NOTIFICACIÓN DE CANCELACIÓN
        enviarNotificacionCancelacion(citaActualizada, emailPaciente, nombrePaciente);

        // ✅ DESBLOQUEAR LA AGENDA para que otro paciente pueda tomarla
        try {
            String desbloquearUrl = String.format(
                    "%s/desbloquear-horario?medicoId=%d&fecha=%s&hora=%s",
                    AGENDA_SERVICE_URL,
                    cita.getMedicoId(),
                    cita.getFecha().toString(),
                    cita.getHora().toString()
            );

            restTemplate.postForObject(desbloquearUrl, null, AgendaDTO.class);

        } catch (Exception e) {
            System.err.println("No se pudo desbloquear la agenda: " + e.getMessage());
            // No fallar la cancelación si no se puede desbloquear
        }

        return citaActualizada;
    }

    /**
     * ✅ MÉTODO DE COMPATIBILIDAD: Cancelar sin email (busca en usuarios)
     */
    @Transactional
    public Cita cancelarCita(Long id) {
        // Por ahora, usamos valores por defecto
        // Idealmente deberías consultar el microservicio de usuarios
        return cancelarCita(id, "paciente@mediplus.com", "Paciente");
    }

    /**
     * ✅ Validar si una cita puede ser cancelada
     */
    public boolean puedeCancelar(Long citaId) {
        try {
            Cita cita = buscarPorId(citaId);

            if ("CANCELADA".equals(cita.getEstado())) {
                return false;
            }

            LocalDateTime fechaHoraCita = LocalDateTime.of(cita.getFecha(), cita.getHora());
            LocalDateTime ahora = LocalDateTime.now();
            LocalDateTime limiteMinimo = fechaHoraCita.minusHours(12);

            return ahora.isBefore(limiteMinimo);

        } catch (Exception e) {
            return false;
        }
    }

    public void eliminarPorId(Long id) {
        citaRepository.deleteById(id);
    }
}