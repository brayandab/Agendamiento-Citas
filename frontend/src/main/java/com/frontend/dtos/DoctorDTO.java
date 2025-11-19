package com.frontend.dtos;

import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    private String especialidad;
    private Long telefono;
    private int aniosExperiencia;
    private String consultorio;
    private String horarioAtencion;

    private Long usuarioId;

}
