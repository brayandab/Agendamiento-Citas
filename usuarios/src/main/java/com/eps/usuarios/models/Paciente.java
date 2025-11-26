package com.eps.usuarios.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad que representa a un Paciente dentro del sistema de usuarios de la EPS.
 *
 * Contiene información personal como documento, teléfono, dirección,
 * fecha de nacimiento y la EPS a la que pertenece.
 *
 * Cada paciente está asociado de forma 1 a 1 con un Usuario,
 * quien es el encargado de autenticarse en el sistema.
 */
@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Paciente {

    /** Identificador único del paciente (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Documento de identidad del paciente */
    @Column(nullable = false, length = 50)
    private String documento;

    /** Teléfono de contacto del paciente */
    @Column(nullable = false, length = 100)
    private String telefono;

    /** Dirección de residencia del paciente */
    @Column(length = 150)
    private String direccion;

    /**
     * Fecha de nacimiento del paciente.
     * Formato JSON: dd/MM/yyyy
     */
    @Column(name = "fecha_nacimiento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaNacimiento;

    /** EPS a la que pertenece el paciente */
    @Column(length = 100)
    private String eps;

    /**
     * Relación 1:1 con la entidad Usuario.
     * Cada paciente tiene exactamente un usuario asociado.
     *
     * - fetch = LAZY para optimizar carga
     * - cascade = ALL para guardar/eliminar automáticamente el usuario
     * - orphanRemoval = true para borrar el usuario si el paciente deja de existir
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;
}
