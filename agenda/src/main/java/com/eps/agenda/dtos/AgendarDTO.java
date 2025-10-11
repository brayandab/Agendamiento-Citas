package com.eps.agenda.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendarDTO {
    @Schema(hidden = true) //
    private Long id;
    private Long medicoId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    // 🔥 Acepta formatos como "10:00 AM" o "3:30 PM"
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")

    private LocalTime horaInicio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime horaFin;

    private Boolean disponible;
}
