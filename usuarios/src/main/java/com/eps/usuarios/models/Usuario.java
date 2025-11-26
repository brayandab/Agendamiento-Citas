package com.eps.usuarios.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa a un Usuario dentro del sistema general de la EPS.
 *
 * El usuario contiene la información esencial para autenticarse:
 * nombre, apellido, correo, contraseña y rol.
 *
 * Esta entidad puede estar asociada tanto a un Paciente como a un Doctor,
 * dependiendo del rol registrado. También soporta un estado de activación.
 */
@Entity
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario {

    /** Identificador único del usuario (PK) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del usuario */
    @Column(nullable = false, length = 100)
    private String nombre;

    /** Apellido del usuario */
    @Column(nullable = false, length = 100)
    private String apellido;

    /**
     * Correo electrónico del usuario.
     * Debe ser único dentro del sistema, ya que se usa para iniciar sesión.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    /** Contraseña cifrada del usuario */
    @Column(nullable = false)
    private String password;

    /**
     * Rol del usuario dentro del sistema.
     * Puede ser ADMIN, DOCTOR, PACIENTE u otro valor definido según requerimientos.
     */
    @Column(nullable = false, length = 100)
    private String rol;

    /**
     * Estado del usuario.
     * true = activo y puede usar el sistema.
     * false = deshabilitado.
     *
     * Se almacena en MySQL como TINYINT(1).
     */
    @Column(nullable = false)
    private Boolean activo = true;
}
