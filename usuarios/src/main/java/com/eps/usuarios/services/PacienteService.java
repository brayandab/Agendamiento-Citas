package com.eps.usuarios.services;

import com.eps.usuarios.dtos.PacienteRequestDTO;
import com.eps.usuarios.models.Paciente;
import com.eps.usuarios.models.Usuario;
import com.eps.usuarios.repository.PacienteRepository;
import com.eps.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de la lógica de negocio relacionada con la entidad {@link Paciente}.
 * <p>
 * Permite crear pacientes, listarlos, eliminarlos y gestionar la relación 1 a 1
 * entre Paciente y Usuario.
 */
@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene todos los pacientes registrados en el sistema.
     *
     * @return lista de objetos {@link Paciente}
     */
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    /**
     * Guarda un paciente directamente en la base de datos.
     *
     * @param paciente entidad paciente ya construida
     * @return el paciente persistido
     */
    public Paciente guardarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    /**
     * Crea un nuevo paciente a partir de un DTO.
     * <p>
     * También valida que el usuario asociado exista previamente.
     *
     * @param dto datos necesarios para crear el paciente
     * @return el paciente creado y almacenado en la base de datos
     */
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

    /**
     * Busca un paciente por su ID.
     *
     * @param id identificador del paciente
     * @return el paciente encontrado
     * @throws RuntimeException si no existe un paciente con ese ID
     */
    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    /**
     * Elimina un paciente por su ID.
     * <p>
     * Gracias al orphanRemoval=true en la entidad, el usuario asociado se elimina automáticamente.
     *
     * @param id identificador del paciente a eliminar
     * @throws RuntimeException si no existe un paciente con ese ID
     */
    public void eliminarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        pacienteRepository.delete(paciente);
        System.out.println("Paciente y usuario eliminados correctamente con ID: " + id);
    }

    /*
     * Método pendiente de implementación si decides agregar búsqueda por correo.
     * public Paciente buscarPorCorreo(String correo) {
     *     return pacienteRepository.buscarPorCorreo(correo);
     * }
     */
}
