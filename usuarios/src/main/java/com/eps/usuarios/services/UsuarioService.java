package com.eps.usuarios.services;

import com.eps.usuarios.dtos.UsuarioRequestDTO;
import com.eps.usuarios.models.Usuario;
import com.eps.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

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

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void eliminarPorId(Long id){
        usuarioRepository.deleteById(id);
        System.out.println("Usuario con id "+id+" fue eliminado");
    }

    public Usuario buscarPorCorreo(String correo){
        String limpio = correo.trim().toLowerCase();
        System.out.println("Correo recibido normalizado: >" + limpio + "<");
        return usuarioRepository.findByCorreo(limpio);
    }

    // 🔥🔥🔥 MÉTODO NUEVO PARA VALIDAR CONTRASEÑA
    public boolean validarPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

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
