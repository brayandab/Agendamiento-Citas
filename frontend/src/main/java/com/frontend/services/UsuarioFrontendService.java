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
