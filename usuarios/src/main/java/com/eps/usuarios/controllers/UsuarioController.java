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

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody UsuarioRequestDTO dto) {
        Usuario guardado = usuarioService.guardarUsuario(dto);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarPorId(@PathVariable Long id){
        usuarioService.eliminarPorId(id);
    }

    @GetMapping("/correo/{correo}")
    public Usuario buscarPorCorreo(@PathVariable String correo) {
        return usuarioService.buscarPorCorreo(correo);
    }

    // 🔥🔥🔥 ENDPOINT NUEVO PARA EL LOGIN
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

        // 👉 Crear el LoginResponseDTO y enviarlo al frontend
        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setApellido(usuario.getApellido());
        response.setCorreo(usuario.getCorreo());
        response.setRol(usuario.getRol());

        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")   // <- CORREGIDO
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioRequestDTO dto) {

        Usuario actualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(actualizado);
    }



}
