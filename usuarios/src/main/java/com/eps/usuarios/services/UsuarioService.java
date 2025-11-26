package com.eps.usuarios.services;

import com.eps.usuarios.dtos.UsuarioRequestDTO;
import com.eps.usuarios.models.Usuario;
import com.eps.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio encargado de manejar la lógica de negocio relacionada con los usuarios.
 * Incluye operaciones CRUD, búsqueda y validación de contraseñas.
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /** Codificador de contraseñas usando BCrypt para garantizar seguridad. */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Obtiene la lista completa de usuarios registrados.
     *
     * @return lista de usuarios
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Crea y almacena un nuevo usuario a partir del DTO recibido.
     * La contraseña es encriptada antes de guardarse.
     *
     * @param dto objeto con los datos necesarios para crear un usuario
     * @return usuario guardado en la base de datos
     */
    public Usuario guardarUsuario(UsuarioRequestDTO dto) {

        Usuario usuario = new Usuario();

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setPassword(dto.getPassword());
        usuario.setCorreo(dto.getCorreo().trim().toLowerCase());
        usuario.setRol(dto.getRol());

        // Encriptar la contraseña
        String hash = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(hash);

        return usuarioRepository.save(usuario);
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id identificador del usuario
     * @return el usuario encontrado o null si no existe
     */
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id identificador del usuario a eliminar
     */
    public void eliminarPorId(Long id){
        usuarioRepository.deleteById(id);
        System.out.println("Usuario con id "+id+" fue eliminado");
    }

    /**
     * Busca un usuario por su correo, normalizando el texto antes de consultar.
     *
     * @param correo correo electrónico del usuario
     * @return usuario encontrado o null si no existe
     */
    public Usuario buscarPorCorreo(String correo){
        String limpio = correo.trim().toLowerCase();
        System.out.println("Correo recibido normalizado: >" + limpio + "<");
        return usuarioRepository.findByCorreo(limpio);
    }

    /**
     * Valida si una contraseña ingresada coincide con la contraseña encriptada almacenada.
     *
     * @param rawPassword contraseña sin encriptar ingresada por el usuario
     * @param hashedPassword contraseña encriptada almacenada en la base de datos
     * @return true si coinciden, false de lo contrario
     */
    public boolean validarPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    /**
     * Actualiza los datos de un usuario existente.
     * Solo actualiza la contraseña si se envía una nueva.
     *
     * @param id identificador del usuario a actualizar
     * @param dto datos actualizados del usuario
     * @return usuario actualizado
     * @throws RuntimeException si no se encuentra el usuario
     */
    public Usuario actualizarUsuario(Long id, UsuarioRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo().trim().toLowerCase());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

}

