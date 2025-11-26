package com.frontend.dtos.request.usuarios;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO utilizado para crear o actualizar información de un paciente.
 * Contiene los datos personales y de contacto del paciente,
 * así como su información de salud básica.
 */
@Data
public class PacienteRequestDTO {

    /**
     * Número de documento de identidad del paciente.
     */
    private String documento;

    /**
     * Número de teléfono de contacto del paciente.
     */
    private String telefono;

    /**
     * Dirección de residencia del paciente.
     */
    private String direccion;

    /**
     * Fecha de nacimiento del paciente.
     * Se maneja con formato "yyyy-MM-dd".
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    /**
     * Nombre de la EPS a la que está afiliado el paciente.
     */
    private String eps;

    /**
     * ID del usuario asociado a este paciente en el sistema.
     */
    private Long usuarioId;
}
