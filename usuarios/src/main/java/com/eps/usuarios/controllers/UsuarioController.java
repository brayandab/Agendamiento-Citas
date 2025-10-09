package com.eps.usuarios.controllers;

import com.eps.usuarios.dtos.UsuarioRequestDTO;
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
}
