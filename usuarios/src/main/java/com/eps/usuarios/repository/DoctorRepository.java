package com.eps.usuarios.repository;

import com.eps.usuarios.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository <Doctor, Long> {

}
