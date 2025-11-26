package com.eps.usuarios.services;

import com.eps.usuarios.dtos.DoctorRequestDTO;
import com.eps.usuarios.models.Doctor;
import com.eps.usuarios.models.Usuario;
import com.eps.usuarios.models.enums.Especialidad;
import com.eps.usuarios.repository.DoctorRepository;
import com.eps.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de manejar la lógica de negocio relacionada con la entidad {@link Doctor}.
 * <p>
 * Permite realizar operaciones CRUD, asignar un usuario a un doctor y
 * consultar doctores por especialidad o por ID de usuario.
 */
@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene la lista completa de doctores registrados.
     *
     * @return lista de objetos {@link Doctor}
     */
    public List<Doctor> listarDoctores() {
        return doctorRepository.findAll();
    }

    /**
     * Busca un doctor por su ID.
     *
     * @param id identificador del doctor
     * @return el doctor encontrado
     * @throws RuntimeException si no existe un doctor con ese ID
     */
    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
    }

    /**
     * Crea un nuevo doctor en el sistema a partir de un DTO.
     * <p>
     * También valida que el usuario asociado exista previamente.
     *
     * @param dto datos necesarios para registrar el doctor
     * @return el doctor creado y persistido en la base de datos
     */
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

    /**
     * Elimina un doctor por su ID.
     *
     * @param id identificador del doctor a borrar
     * @throws RuntimeException si no existe un doctor con ese ID
     */
    public void deleteById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        doctorRepository.deleteById(id);
        System.out.println("Doctor y usuario eliminados correctamente con ID: " + id);
    }

    /**
     * Busca doctores por su especialidad.
     *
     * @param especialidad tipo de especialidad médica
     * @return lista de doctores que coinciden con la especialidad dada
     */
    public List<Doctor> findByEspecialidad(Especialidad especialidad) {
        return doctorRepository.findByEspecialidad(especialidad);
    }

    /**
     * Busca un doctor asociado al ID de un usuario.
     *
     * @param usuarioId ID del usuario relacionado al doctor
     * @return el doctor encontrado o {@code null} si no existe relación
     */
    public Doctor findByUsuarioId(Long usuarioId) {
        return doctorRepository.findByUsuarioId(usuarioId).orElse(null);
    }
}
