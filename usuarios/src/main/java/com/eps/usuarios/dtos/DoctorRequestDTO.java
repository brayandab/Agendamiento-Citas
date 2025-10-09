package com.eps.usuarios.dtos;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;
import java.util.Map;
@Data
public class DoctorRequestDTO {


    private String especialidad;

    private Long telefono;

    private int aniosExperiencia;

    private String consultorio;

    private String horarioAtencion;

    private Long usuarioId;
}
