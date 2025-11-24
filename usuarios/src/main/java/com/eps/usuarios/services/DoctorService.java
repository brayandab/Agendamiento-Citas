package com.eps.usuarios.services;

import com.eps.usuarios.dtos.DoctorRequestDTO;
import com.eps.usuarios.dtos.PacienteRequestDTO;
import com.eps.usuarios.models.Doctor;
import com.eps.usuarios.models.Paciente;
import com.eps.usuarios.models.Usuario;
import com.eps.usuarios.models.enums.Especialidad;
import com.eps.usuarios.repository.DoctorRepository;
import com.eps.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Doctor> listarDoctores(){
        return doctorRepository.findAll();
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
    }

    public Doctor createDoctor(DoctorRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Doctor doctor = new Doctor();

        doctor.setEspecialidad(dto.getEspecialidad());
        doctor.setTelefono(dto.getTelefono());
        doctor.setAniosExperiencia(dto.getAniosExperiencia());
        doctor.setConsultorio(dto.getConsultorio());
        doctor.setHorarioAtencion(dto.getHorarioAtencion());
        doctor.setUsuario(usuario);

        return doctorRepository.save(doctor);
    }

    public void deleteById(Long id){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
        doctorRepository.deleteById(id);
        System.out.println(" Doctor y usuario eliminados correctamente con ID: " + id);
    }

    public List<Doctor> findByEspecialidad(Especialidad especialidad) {
        return doctorRepository.findByEspecialidad(especialidad);
    }
    public Doctor findByUsuarioId(Long usuarioId) {
        return doctorRepository.findByUsuarioId(usuarioId)
                .orElse(null);
    }


}
