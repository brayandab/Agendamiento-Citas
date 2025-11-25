package com.frontend.dtos.request.cita;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaRequestDTO {
    private Long pacienteId;
    private Long medicoId;
    private String especialidad;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "[HH:mm:ss][HH:mm]")
    private LocalTime hora;

    private String motivo;

    // ✅ Campos para notificaciones
    private String emailPaciente;
    private String nombrePaciente;
    private String nombreDoctor;
}