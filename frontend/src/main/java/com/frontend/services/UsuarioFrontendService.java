package com.frontend.services;

import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.request.usuarios.DoctorRequestDTO;
import com.frontend.dtos.request.usuarios.PacienteRequestDTO;
import com.frontend.dtos.request.usuarios.UsuarioRequestDTO;
import com.frontend.dtos.response.usuarios.DoctorDTO;
import com.frontend.dtos.response.usuarios.PacienteDTO;
import com.frontend.dtos.response.usuarios.UsuarioDTO;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio de frontend para interactuar con el microservicio de usuarios.
 * Actúa como capa intermedia entre los controladores y el cliente Feign,
 * encapsulando la lógica de llamadas al microservicio.
 */
@Service
public class UsuarioFrontendService {

    private final UsuariosClient usuarioClient;

    /**
     * Constructor que inyecta el cliente Feign para usuarios.
     * @param usuarioClient Cliente Feign que se comunica con el microservicio "usuarios"
     */
    public UsuarioFrontendService(UsuariosClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    // ---------- USUARIOS ----------

    /**
     * Obtiene la lista completa de usuarios del sistema.
     * @return Lista de UsuarioDTO
     */
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioClient.listarUsuarios();
    }

    /**
     * Busca un usuario por su ID.
     * @param id ID del usuario
     * @return UsuarioDTO correspondiente
     */
    public UsuarioDTO buscarUsuario(Long id) {
        return usuarioClient.findById(id);
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * @param dto Datos del usuario a crear
     * @return UsuarioDTO creado
     */
    public UsuarioDTO crearUsuario(UsuarioRequestDTO dto) {
        return usuarioClient.crearUsuario(dto);
    }

    // ---------- DOCTORES ----------

    /**
     * Obtiene todos los doctores registrados.
     * @return Lista de DoctorDTO
     */
    public List<DoctorDTO> listarDoctores() {
        return usuarioClient.listarDoctores();
    }

    /**
     * Busca un doctor por su ID.
     * @param id ID del doctor
     * @return DoctorDTO correspondiente
     */
    public DoctorDTO buscarDoctor(Long id) {
        return usuarioClient.buscarDoctorPorId(id);
    }

    /**
     * Crea un nuevo doctor en el sistema.
     * @param dto Datos del doctor a crear
     * @return DoctorDTO creado
     */
    public DoctorDTO crearDoctor(DoctorRequestDTO dto) {
        return usuarioClient.crearDoctor(dto);
    }

    /**
     * Elimina un doctor por su ID.
     * @param id ID del doctor a eliminar
     */
    public void eliminarDoctor(Long id) {
        usuarioClient.eliminarDoctor(id);
    }

    // ---------- PACIENTES ----------

    /**
     * Obtiene todos los pacientes registrados.
     * @return Lista de PacienteDTO
     */
    public List<PacienteDTO> listarPacientes() {
        return usuarioClient.listarPacientes();
    }

    /**
     * Busca un paciente por su ID.
     * @param id ID del paciente
     * @return PacienteDTO correspondiente
     */
    public PacienteDTO buscarPaciente(Long id) {
        return usuarioClient.buscarPacientePorId(id);
    }

    /**
     * Crea un nuevo paciente en el sistema.
     * @param dto Datos del paciente a crear
     * @return PacienteDTO creado
     */
    public PacienteDTO crearPaciente(PacienteRequestDTO dto) {
        return usuarioClient.crearPaciente(dto);
    }

    /**
     * Elimina un paciente por su ID.
     * @param id ID del paciente a eliminar
     */
    public void eliminarPaciente(Long id) {
        usuarioClient.eliminarPaciente(id);
    }
}
