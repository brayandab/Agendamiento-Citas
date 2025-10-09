package com.eps.usuarios.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String especialidad;

    @Column(nullable = false, length = 15)
    private Long telefono;

    @Column(nullable = false, length = 2, name = "años_experiencia")
    private int aniosExperiencia;

    @Column(nullable = false, length = 50)
    private String consultorio;

    @Column(nullable = false, name = "horario_atención")
    private String horarioAtencion;

    // 🔹 Relación 1 a 1 con Usuario
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;
}
