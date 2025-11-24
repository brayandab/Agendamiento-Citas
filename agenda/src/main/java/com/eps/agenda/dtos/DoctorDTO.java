package com.eps.agenda.dtos;

import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    private String especialidad;
    private Long telefono;
    private Integer aniosExperiencia;
    private String consultorio;
    private String horarioAtencion;
    private UsuarioDTO usuario;
}
