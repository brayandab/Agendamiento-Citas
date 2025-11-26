package com.frontend.dtos.response.usuarios;

import lombok.Data;

/**
 * DTO que representa la información de un paciente dentro del sistema.
 * Se utiliza principalmente en respuestas del backend hacia el frontend
 * para mostrar detalles de los pacientes en paneles de administración
 * o para relacionar citas con los pacientes correspondientes.
 */
@Data
public class PacienteDTO {

    /**
     * Identificador único del paciente.
     */
    private Long id;

    /**
     * Número de documento de identidad del paciente.
     */
    private String documento;

    /**
     * Número de teléfono del paciente.
     */
    private String telefono;

    /**
     * Dirección de residencia del paciente.
     */
    private String direccion;

    /**
     * Fecha de nacimiento del paciente en formato String "yyyy-MM-dd".
     */
    private String fechaNacimiento;

    /**
     * Nombre de la EPS o aseguradora del paciente.
     */
    private String eps;

    /**
     * Identificador del usuario asociado al paciente.
     * Permite vincular la información de login y otros datos de usuario.
     */
    private Long usuarioId;
}
