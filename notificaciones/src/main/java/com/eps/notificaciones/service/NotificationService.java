package com.eps.notificaciones.service;

import com.eps.notificaciones.dtos.NotificationRequest;
import com.eps.notificaciones.model.NotificacionTipo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String generarMensaje(NotificationRequest request) {
        String plantilla = "";
        String asunto = "";

        switch (request.getTipo()) {
            case CONFIRMACION_CITA:
                asunto = "Confirmación de cita médica";
                plantilla = String.format("""
                    Hola %s,
                    
                    Tu cita médica ha sido CONFIRMADA para el día %s.
                    
                    Recuerda llegar 20 minutos antes de la cita para facturación y evitar contratiempos.
                    
                    Para reprogramar o cancelar, hazlo con un (1) día de anticipación.
                    
                    ¡Gracias por confiar en nosotros!
                    
                    Cordialmente,
                    
                    Equipo de agendamiento MediPlus.
                    """, request.getNamePatient(), request.getApptDate());
                break;

            case CANCELACION_CITA:
                asunto = "Cancelación de cita médica";
                plantilla = String.format("""
                    Hola %s,
                    
                    Tu cita médica programada para el día %s ha sido CANCELADA.
                    
                    Si deseas reprogramarla, contáctanos nuevamente.
                    
                    Cordialmente,
                    
                    Equipo de agendamiento MediPlus
                    """, request.getNamePatient(), request.getApptDate());
                break;

            case AGENDAMIENTO_CITA:
                asunto = "Nueva cita médica agendada";
                plantilla = String.format("""
                    Hola %s,
                    
                    Has AGENDADO una nueva cita para el día %s.
                    
                    Recibirás una confirmación cuando sea aprobada.
                    
                    Cordialmente,
                    
                    Equipo de agendamiento MediPlus.
                    """, request.getNamePatient(), request.getApptDate());
                break;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getTo());
        message.setSubject(asunto);
        message.setText(plantilla);

        mailSender.send(message);

        return String.format("""
            ✅ Correo enviado exitosamente a: %s
            Asunto: %s
            
            %s
            """, request.getTo(), asunto, plantilla);
    }
}
