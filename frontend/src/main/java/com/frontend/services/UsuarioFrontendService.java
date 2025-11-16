package com.frontend.services;


import com.frontend.clients.UsuariosClient;
import com.frontend.dtos.*;
import com.frontend.dtos.request.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioFrontendService {

    private final UsuariosClient usuarioClient;

    public UsuarioFrontendService(UsuariosClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    // ---------- USUARIOS ----------

    public List<UsuarioDTO> listarUsuarios() {
        return usuarioClient.listarUsuarios();
    }

    public UsuarioDTO buscarUsuario(Long id) {
        return usuarioClient.findById(id);
    }

    public UsuarioDTO crearUsuario(UsuarioRequestDTO dto) {
        return usuarioClient.crearUsuario(dto);
    }

    //Falta eliminar


    // ---------- DOCTORES ----------

    public List<DoctorDTO> listarDoctores() {
        return usuarioClient.listarDoctores();
    }

    public DoctorDTO buscarDoctor(Long id) {
        return usuarioClient.buscarDoctorPorId(id);
    }

    public DoctorDTO crearDoctor(DoctorRequestDTO dto) {
        return usuarioClient.crearDoctor(dto);
    }

    public void eliminarDoctor(Long id) {
        usuarioClient.eliminarDoctor(id);
    }


    // ---------- PACIENTES ----------

    public List<PacienteDTO> listarPacientes() {
        return usuarioClient.listarPacientes();
    }

    public PacienteDTO buscarPaciente(Long id) {
        return usuarioClient.buscarPacientePorId(id);
    }

    public PacienteDTO crearPaciente(PacienteRequestDTO dto) {
        return usuarioClient.crearPaciente(dto);
    }

    public void eliminarPaciente(Long id) {
        usuarioClient.eliminarPaciente(id);
    }
}
