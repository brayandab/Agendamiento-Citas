package com.eps.usuarios.repository;

import com.eps.usuarios.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository <Doctor, Long> {

    List<Doctor> findByEspecialidadIgnoreCase(String especialidad);


}
