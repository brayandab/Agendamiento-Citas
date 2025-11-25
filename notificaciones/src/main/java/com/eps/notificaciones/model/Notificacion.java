package com.eps.notificaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destinatario;

    @Column(nullable = false)
    private String nombrePaciente;

    @Column(nullable = false)
    private String fechaCita;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificacionTipo tipo;

    @Column(nullable = false)
    private String asunto;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @Column(nullable = false)
    private String estado;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "error_mensaje", columnDefinition = "TEXT")
    private String errorMensaje;

    private Long citaId;
    private Long pacienteId;
    private String especialidad;
}
