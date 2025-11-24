package com.eps.usuarios.repository;

import com.eps.usuarios.models.Doctor;
import com.eps.usuarios.models.enums.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository <Doctor, Long> {

    List<Doctor> findByEspecialidad(Especialidad especialidad);

    Optional<Doctor> findByUsuarioId(Long usuarioId);



}
