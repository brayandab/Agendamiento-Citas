package com.frontend.dtos.request.usuarios;


import lombok.Data;

@Data
public class DoctorRequestDTO {
    private String especialidad;
    private Long telefono;
    private int aniosExperiencia;
    private String consultorio;
    private String horarioAtencion;

    private Long usuarioId;
}
