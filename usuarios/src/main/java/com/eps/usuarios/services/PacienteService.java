package com.eps.usuarios.services;

import com.eps.usuarios.dtos.PacienteRequestDTO;
import com.eps.usuarios.models.Paciente;
import com.eps.usuarios.models.Usuario;
import com.eps.usuarios.repository.PacienteRepository;
import com.eps.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    //  Listar todos los pacientes
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    //  Guardar un paciente directamente
    public Paciente guardarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    //  Crear paciente a partir del DTO
    public Paciente crearPaciente(PacienteRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Paciente paciente = new Paciente();
        paciente.setDocumento(dto.getDocumento());
        paciente.setTelefono(dto.getTelefono());
        paciente.setDireccion(dto.getDireccion());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setEps(dto.getEps());
        paciente.setUsuario(usuario);

        return pacienteRepository.save(paciente);
    }

    //  Buscar un paciente por ID
    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    //  Eliminar paciente (y su usuario asociado automáticamente)
    public void eliminarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        pacienteRepository.delete(paciente);
        System.out.println(" Paciente y usuario eliminados correctamente con ID: " + id);
    }

    /*public Paciente buscarPorCorreo(String correo) {
        return pacienteRepository.buscarPorCorreo(correo);
    }*/
}
