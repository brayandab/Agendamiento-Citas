package com.eps.usuarios.repository;

import com.eps.usuarios.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la gestión de la entidad {@link Usuario}.
 *
 * Proporciona operaciones CRUD y capacidades de paginación/ordenamiento
 * gracias a la herencia de {@link JpaRepository}.
 *
 * Métodos personalizados:
 * - {@link #findByCorreo(String)}: permite buscar un usuario por su correo electrónico,
 *   útil para procesos de autenticación o validación previa de registro.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo correo del usuario a buscar. Debe ser único en el sistema.
     * @return el usuario correspondiente, o {@code null} si no existe.
     */
    Usuario findByCorreo(String correo);
}
