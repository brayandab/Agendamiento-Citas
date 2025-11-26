package com.eps.usuarios.models;


import com.eps.usuarios.models.enums.Especialidad;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * Entidad que representa a un doctor dentro del sistema de la EPS.
 *
 * <p>Esta clase almacena la información relevante de cada profesional de la salud,
 * incluyendo su especialidad, experiencia, datos de contacto y su relación con el
 * usuario asociado.</p>
 *
 * <p><b>Características principales:</b></p>
 * <ul>
 *     <li>Identificador único autogenerado.</li>
 *     <li>Especialidad médica representada mediante un {@link Especialidad}.</li>
 *     <li>Datos de contacto como teléfono y consultorio.</li>
 *     <li>Años de experiencia registrados como un valor numérico.</li>
 *     <li>Horario de atención especificado como cadena.</li>
 *     <li>Relación uno a uno con la entidad {@link Usuario}, representando la
 *         cuenta del sistema asociada al doctor.</li>
 * </ul>
 *
 * <p><b>Relaciones:</b></p>
 * <ul>
 *     <li><b>Usuario</b>: relación 1:1. Cada doctor está vinculado a un único usuario
 *     del sistema para autenticación y datos generales.</li>
 * </ul>
 *
 * <p>La entidad usa anotaciones JPA para mapearse a la tabla <code>doctores</code>
 * y Lombok para la generación automática de getters, setters y constructores.</p>
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Doctor {

    /**
     * Identificador único del doctor en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Especialidad médica del doctor.
     * Se almacena como texto usando EnumType.STRING para mayor legibilidad.
     */
    @Column(nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;

    /**
     * Teléfono de contacto del doctor.
     */
    @Column(nullable = false, length = 15)
    private Long telefono;

    /**
     * Años de experiencia del profesional.
     */
    @Column(nullable = false, length = 2, name = "años_experiencia")
    private int aniosExperiencia;

    /**
     * Consultorio donde atiende el doctor.
     */
    @Column(nullable = false, length = 50)
    private String consultorio;

    /**
     * Horario de atención del doctor.
     */
    @Column(nullable = false, name = "horario_atención")
    private String horarioAtencion;

    /**
     * Relación uno a uno con el usuario asociado al doctor.
     * Cada doctor tiene un único usuario en el sistema.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;
}
