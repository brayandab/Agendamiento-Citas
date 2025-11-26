package com.eps.usuarios.repository;

import com.eps.usuarios.models.Doctor;
import com.eps.usuarios.models.enums.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la gestión de la entidad {@link Doctor}.
 *
 * Proporciona métodos CRUD básicos mediante {@link JpaRepository},
 * y consultas personalizadas para buscar doctores según criterios específicos.
 */
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /**
     * Busca todos los doctores que pertenecen a una especialidad específica.
     *
     * @param especialidad especialidad médica del doctor
     * @return lista de doctores que coinciden con la especialidad indicada
     */
    List<Doctor> findByEspecialidad(Especialidad especialidad);

    /**
     * Busca un doctor por el identificador de su usuario asociado.
     *
     * Esta consulta se usa en casos como login, validación o relación
     * 1 a 1 entre Usuario y Doctor.
     *
     * @param usuarioId ID del usuario asociado al doctor
     * @return un Optional con el doctor encontrado o vacío si no existe
     */
    Optional<Doctor> findByUsuarioId(Long usuarioId);
}
