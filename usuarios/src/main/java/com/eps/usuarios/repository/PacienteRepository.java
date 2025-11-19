package com.eps.usuarios.repository;

import com.eps.usuarios.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository <Paciente,Long> {

}
