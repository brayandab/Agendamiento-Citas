package com.eps.usuarios.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;

/**
 * DTO utilizado para la creación y actualización de pacientes dentro del sistema.
 * Este objeto es enviado desde el frontend hacia el backend y contiene
 * los datos necesarios para registrar un nuevo paciente asociado
 * a un usuario previamente creado.
 */
@Data
public class PacienteRequestDTO {

    /**
     * Documento de identidad del paciente.
     * Puede representar cédula, tarjeta de identidad u otro tipo de identificación.
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
     * Fecha de nacimiento del paciente en formato yyyy-MM-dd.
     * Se utiliza para validar edad y datos epidemiológicos.
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    /**
     * EPS a la cual está afiliado el paciente.
     */
    private String eps;

    /**
     * ID del usuario asociado al paciente.
     * Este valor se relaciona con la tabla de usuarios (autenticación).
     */
    private Long usuarioId;
}
