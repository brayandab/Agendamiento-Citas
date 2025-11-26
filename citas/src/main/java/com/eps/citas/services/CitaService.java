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

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con las citas
 * médicas: creación, consulta, validación, bloqueo de agenda y envío de notificaciones.
 *
 * <p>Incluye integración con:
 * <ul>
 *     <li><b>Microservicio de agenda</b>: bloqueo y desbloqueo de horarios.</li>
 *     <li><b>Microservicio de notificaciones</b>: envío de correos para
 *         agendamiento y cancelación.</li>
 * </ul>
 *
 * <p>Este servicio también maneja reglas de negocio como:
 * <ul>
 *     <li>Validación de disponibilidad del horario.</li>
 *     <li>Cancelación con mínimo 12 horas de anticipación.</li>
 *     <li>Evitar re-cancelación de citas.</li>
 * </ul>
 */
@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String AGENDA_SERVICE_URL = "http://localhost:8082/agenda";
    private final String NOTIFICACIONES_URL = "http://localhost:8084/api/notificaciones";

    /**
     * Obtiene la lista completa de citas almacenadas en la base de datos.
     *
     * @return lista de citas existentes
     */
    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    /**
     * Crea una nueva cita médica.
     *
     * <p>Flujo del proceso:
     * <ol>
     *     <li>Bloquea el horario en el microservicio de agenda.</li>
     *     <li>Registra la cita en la base de datos.</li>
     *     <li>Envía correo de confirmación al paciente.</li>
     * </ol>
     *
     * @param dto datos de creación de la cita
     * @return la cita creada y guardada
     * @throws RuntimeException si el horario no está disponible o hay errores al guardar
     */
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
     * Envía una notificación al paciente indicando que la cita fue agendada correctamente.
     *
     * @param cita cita creada
     * @param dto  datos del paciente para la notificación
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

        } catch (Exception e) {
            // No se detiene el flujo si falla la notificación
        }
    }

    /**
     * Envía la notificación correspondiente a la cancelación de una cita.
     *
     * @param cita           cita cancelada
     * @param emailPaciente  email del paciente
     * @param nombrePaciente nombre del paciente
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

        } catch (Exception e) {
            // No falla la cancelación si hay error en notificación
        }
    }

    /**
     * Obtiene una cita por su ID.
     *
     * @param id identificador de la cita
     * @return la cita encontrada
     * @throws RuntimeException si no existe una cita con ese ID
     */
    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));
    }

    /**
     * Obtiene todas las citas asociadas a un paciente.
     *
     * @param pacienteId id del paciente
     * @return lista de citas relacionadas
     */
    public List<Cita> buscarPorPacienteId(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }

    /**
     * Obtiene todas las citas asociadas a un médico.
     *
     * @param medicoId id del médico
     * @return lista de citas relacionadas
     */
    public List<Cita> buscarPorMedicoId(Long medicoId) {
        return citaRepository.findByMedicoId(medicoId);
    }

    /**
     * Cancela una cita validando la regla de negocio:
     * debe hacerse con mínimo 12 horas de anticipación.
     *
     * <p>Pasos:
     * <ol>
     *     <li>Valida que la cita existe.</li>
     *     <li>Verifica que no esté ya cancelada.</li>
     *     <li>Comprueba el límite de 12 horas.</li>
     *     <li>Actualiza el estado.</li>
     *     <li>Envía notificación.</li>
     *     <li>Desbloquea el horario en agenda.</li>
     * </ol>
     *
     * @param id             id de la cita
     * @param emailPaciente  email del paciente
     * @param nombrePaciente nombre del paciente
     * @return cita cancelada
     * @throws RuntimeException si no cumple la regla de anticipación
     */
    @Transactional
    public Cita cancelarCita(Long id, String emailPaciente, String nombrePaciente) {
        Cita cita = buscarPorId(id);

        if ("CANCELADA".equals(cita.getEstado())) {
            throw new RuntimeException("Esta cita ya está cancelada");
        }

        LocalDateTime fechaHoraCita = LocalDateTime.of(cita.getFecha(), cita.getHora());
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limiteMinimo = fechaHoraCita.minusHours(12);

        if (ahora.isAfter(limiteMinimo)) {
            throw new RuntimeException("No se puede cancelar la cita. Debe hacerlo con al menos 12 horas de anticipación");
        }

        cita.setEstado("CANCELADA");
        Cita citaActualizada = citaRepository.save(cita);

        enviarNotificacionCancelacion(citaActualizada, emailPaciente, nombrePaciente);

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
            // No se detiene la cancelación si falla el desbloqueo
        }

        return citaActualizada;
    }

    /**
     * Método alternativo para cancelar una cita sin especificar datos del paciente.
     * Usado como compatibilidad temporal.
     *
     * @param id id de la cita
     * @return cita cancelada
     */
    @Transactional
    public Cita cancelarCita(Long id) {
        return cancelarCita(id, "paciente@mediplus.com", "Paciente");
    }

    /**
     * Valida si una cita puede ser cancelada considerando la regla
     * de anticipación de 12 horas.
     *
     * @param citaId id de la cita
     * @return true si puede cancelar, false si no
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

    /**
     * Elimina una cita definitivamente de la base de datos.
     *
     * @param id id de la cita
     */
    public void eliminarPorId(Long id) {
        citaRepository.deleteById(id);
    }
}
