package com.eps.citas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entidad que representa una cita médica registrada en el sistema.
 *
 * Esta clase se mapea a la tabla "citas" en la base de datos y contiene
 * los datos esenciales de la cita: paciente, médico, fecha, hora, motivo
 * y estado actual.
 *
 * La entidad se utiliza tanto para la persistencia interna como para
 * responder al cliente en las operaciones de listados, consultas
 * y actualizaciones.
 */
@Entity
@Table(name = "citas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cita {

    /** Identificador único de la cita (primary key autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID del paciente asociado a la cita. */
    @Column(name = "paciente_id")
    private Long pacienteId;

    /** ID del médico que atenderá la cita. */
    @Column(name = "medico_id")
    private Long medicoId;

    /** Especialidad médica correspondiente a la cita. */
    private String especialidad;

    /** Fecha en la que se realizará la cita. */
    @Column(name = "fecha")
    private LocalDate fecha;

    /** Hora programada para la cita. */
    private LocalTime hora;

    /**
     * Estado actual de la cita.
     *
     * Ejemplos:
     * - PROGRAMADA
     * - CANCELADA
     * - FINALIZADA
     */
    private String estado;

    /** Motivo o descripción breve de la consulta médica. */
    private String motivo;
}
