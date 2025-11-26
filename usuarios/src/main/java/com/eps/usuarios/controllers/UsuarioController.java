package com.eps.usuarios.controllers;

import com.eps.usuarios.dtos.LoginRequestDTO;
import com.eps.usuarios.dtos.UsuarioRequestDTO;
import com.eps.usuarios.dtos.response.LoginResponseDTO;
import com.eps.usuarios.models.Usuario;
import com.eps.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas
 * con los usuarios del sistema.
 *
 * Permite crear, listar, actualizar, eliminar y autenticar usuarios.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene una lista completa de todos los usuarios registrados.
     *
     * @return lista de objetos {@link Usuario}.
     */
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarUsuarios();
    }

    /**
     * Crea un nuevo usuario utilizando los datos proporcionados en el DTO.
     *
     * @param dto información necesaria para registrar un usuario.
     * @return el usuario creado dentro de un {@link ResponseEntity}.
     */
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody UsuarioRequestDTO dto) {
        Usuario guardado = usuarioService.guardarUsuario(dto);
        return ResponseEntity.ok(guardado);
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id identificador único del usuario.
     * @return el usuario encontrado o null si no existe.
     */
    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id identificador del usuario a eliminar.
     */
    @DeleteMapping("/{id}")
    public void eliminarPorId(@PathVariable Long id){
        usuarioService.eliminarPorId(id);
    }

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo correo del usuario a consultar.
     * @return el usuario correspondiente al correo.
     */
    @GetMapping("/correo/{correo}")
    public Usuario buscarPorCorreo(@PathVariable String correo) {
        return usuarioService.buscarPorCorreo(correo);
    }

    /**
     * Endpoint de autenticación para validar las credenciales de un usuario.
     *
     * Verifica si el correo existe y si la contraseña coincide.
     * En caso exitoso, retorna un DTO con los datos relevantes del usuario.
     *
     * @param login DTO con correo y contraseña.
     * @return respuesta con información del usuario autenticado o error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO login) {

        Usuario usuario = usuarioService.buscarPorCorreo(login.getCorreo());

        if (usuario == null) {
            return ResponseEntity.status(404).body("Correo no encontrado");
        }

        boolean passwordOk = usuarioService.validarPassword(
                login.getPassword(),
                usuario.getPassword()
        );

        if (!passwordOk) {
            return ResponseEntity.status(400).body("Contraseña incorrecta");
        }

        // Construcción del DTO de respuesta
        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setApellido(usuario.getApellido());
        response.setCorreo(usuario.getCorreo());
        response.setRol(usuario.getRol());

        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param id  identificador del usuario a actualizar.
     * @param dto datos nuevos para el usuario.
     * @return usuario actualizado dentro de un {@link ResponseEntity}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioRequestDTO dto) {

        Usuario actualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(actualizado);
    }
}
