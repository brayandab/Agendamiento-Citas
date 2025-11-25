package com.eps.notificaciones.service;

import com.eps.notificaciones.dtos.NotificationRequest;
import com.eps.notificaciones.dtos.NotificationResponse;
import com.eps.notificaciones.model.Notificacion;
import com.eps.notificaciones.model.NotificacionTipo;
import com.eps.notificaciones.repository.NotificacionRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;
    private final NotificacionRepository notificacionRepository;

    public NotificationService(JavaMailSender mailSender, NotificacionRepository notificacionRepository) {
        this.mailSender = mailSender;
        this.notificacionRepository = notificacionRepository;
    }


      //MÉTODO MEJORADO:Genera mensaje y guarda en BD

    @Transactional
    public NotificationResponse enviarNotificacion(NotificationRequest request) {
        String plantilla = "";
        String asunto = "";

        try {
            // Generar contenido del mensaje
            switch (request.getTipo()) {
                case AGENDAMIENTO_CITA:
                    asunto = "Nueva cita médica agendada - MediPlus";
                    plantilla = generarMensajeAgendamiento(request);
                    break;

                case CONFIRMACION_CITA:
                    asunto = "Confirmación de cita médica - MediPlus";
                    plantilla = generarMensajeConfirmacion(request);
                    break;

                case CANCELACION_CITA:
                    asunto = "Cancelación de cita médica - MediPlus";
                    plantilla = generarMensajeCancelacion(request);
                    break;

                default:
                    throw new IllegalArgumentException("Tipo de notificación no válido");
            }

            // Enviar correo
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getTo());
            message.setSubject(asunto);
            message.setText(plantilla);
            message.setFrom("confirmacioncitas.mediplus@gmail.com");

            mailSender.send(message);

            // Guardar en BD como ENVIADO
            Notificacion notificacion = guardarNotificacion(request, asunto, plantilla, "ENVIADO", null);

            return new NotificationResponse(
                    true,
                    "Correo enviado exitosamente a: " + request.getTo(),
                    notificacion.getId()
            );

        } catch (Exception e) {
            // Guardar en BD como FALLIDO
            Notificacion notificacion = guardarNotificacion(request, asunto, plantilla, "FALLIDO", e.getMessage());

            return new NotificationResponse(
                    false,
                    "Error al enviar correo: " + e.getMessage(),
                    notificacion.getId()
            );
        }
    }

    /**
     * Guardar notificación en base de datos
     */
    private Notificacion guardarNotificacion(NotificationRequest request, String asunto,
                                             String mensaje, String estado, String error) {
        Notificacion notificacion = new Notificacion();
        notificacion.setDestinatario(request.getTo());
        notificacion.setNombrePaciente(request.getNamePatient());
        notificacion.setFechaCita(request.getApptDate());
        notificacion.setTipo(request.getTipo());
        notificacion.setAsunto(asunto);
        notificacion.setMensaje(mensaje);
        notificacion.setEstado(estado);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setErrorMensaje(error);

        // Información adicional
        notificacion.setCitaId(request.getCitaId());
        notificacion.setPacienteId(request.getPacienteId());
        notificacion.setEspecialidad(request.getEspecialidad());

        return notificacionRepository.save(notificacion);
    }

    /**
     * Plantilla para agendamiento
     */
    private String generarMensajeAgendamiento(NotificationRequest request) {
        return String.format("""
            🏥 ¡Hola %s!
            
            Tu cita médica ha sido AGENDADA exitosamente.
            
            📋 Detalles de la cita:
            • Especialidad: %s
            • Fecha: %s
            • Hora: %s
            %s
            
            ⚠️ IMPORTANTE:
            • Recuerda llegar 15 minutos antes de tu cita
            • Puedes cancelar con al menos 12 horas de anticipación
            • Si no puedes asistir, cancela tu cita para que otro paciente pueda tomarla
            
            📱 Accede a tu perfil en MediPlus para ver más detalles.
            
            ¡Gracias por confiar en nosotros!
            
            Cordialmente,
            Equipo MediPlus
            
            ---
            Este es un mensaje automático, por favor no responder.
            """,
                request.getNamePatient(),
                request.getEspecialidad() != null ? request.getEspecialidad() : "No especificada",
                request.getApptDate(),
                request.getHora() != null ? request.getHora() : "No especificada",
                request.getNombreDoctor() != null ? "• Doctor: " + request.getNombreDoctor() : ""
        );
    }

    /**
     * Plantilla para confirmación
     */
    private String generarMensajeConfirmacion(NotificationRequest request) {
        return String.format("""
            🏥 ¡Hola %s!
            
            Tu cita médica ha sido CONFIRMADA.
            
            📋 Detalles de la cita:
            • Especialidad: %s
            • Fecha: %s
            • Hora: %s
            
            ⚠️ Recordatorios:
            • Llega 15 minutos antes para el proceso de facturación
            • Trae tu documento de identidad y carnet de la EPS
            • Si no puedes asistir, cancela con 12 horas de anticipación
            
            📱 Consulta tu cita en el portal de MediPlus
            
            ¡Te esperamos!
            
            Cordialmente,
            Equipo MediPlus
            
            ---
            Este es un mensaje automático, por favor no responder.
            """,
                request.getNamePatient(),
                request.getEspecialidad() != null ? request.getEspecialidad() : "No especificada",
                request.getApptDate(),
                request.getHora() != null ? request.getHora() : "No especificada"
        );
    }

    /**
     * Plantilla para cancelación
     */
    private String generarMensajeCancelacion(NotificationRequest request) {
        return String.format("""
            🏥 Hola %s,
            
            Tu cita médica ha sido CANCELADA exitosamente.
            
            📋 Detalles de la cita cancelada:
            • Especialidad: %s
            • Fecha: %s
            • Hora: %s
            
            Si deseas reprogramar tu cita:
            1. Ingresa a tu cuenta en MediPlus
            2. Ve a "Agendar Cita"
            3. Selecciona la especialidad y el horario que prefieras
            
            Si tienes alguna emergencia médica, no dudes en contactarnos.
            
            Cordialmente,
            Equipo MediPlus
            
            ---
            Este es un mensaje automático, por favor no responder.
            """,
                request.getNamePatient(),
                request.getEspecialidad() != null ? request.getEspecialidad() : "No especificada",
                request.getApptDate(),
                request.getHora() != null ? request.getHora() : "No especificada"
        );
    }

    /**
     * Mantener compatibilidad con método anterior
     */
    @Deprecated
    public String generarMensaje(NotificationRequest request) {
        NotificationResponse response = enviarNotificacion(request);
        return response.getMensaje();
    }
}