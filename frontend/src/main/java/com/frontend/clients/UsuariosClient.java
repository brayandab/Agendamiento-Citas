/*
 * Cliente Feign para comunicarse con el microservicio de Usuarios.
 *
 * Esta clase se encarga de interactuar con el microservicio correspondiente (Usuarios),
 * utilizando el nombre y la URL del servicio. Se recomienda que todas las rutas del microservicio
 * comiencen con el nombre del microservicio para mantener consistencia, por ejemplo:
 *
 * http://localhost:8081/usuarios
 * http://localhost:8081/usuarios/pacientes
 * http://localhost:8081/usuarios/doctores
 *
 * Esto evita la necesidad de crear múltiples clientes Feign por cada entidad.
 */

package com.frontend.clients;

import com.frontend.dtos.response.usuarios.UsuarioDTO;
import com.frontend.dtos.response.usuarios.PacienteDTO;
import com.frontend.dtos.response.usuarios.DoctorDTO;
import com.frontend.dtos.request.LoginRequestDTO;
import com.frontend.dtos.request.usuarios.UsuarioRequestDTO;
import com.frontend.dtos.request.usuarios.PacienteRequestDTO;
import com.frontend.dtos.request.usuarios.DoctorRequestDTO;
import com.frontend.dtos.response.LoginResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Cliente Feign para interactuar con el microservicio de Usuarios.
 * Proporciona métodos para manejar usuarios, doctores, pacientes y login.
 */
@FeignClient(
        name = "usuarios",
        url = "http://localhost:8081"
)
public interface UsuariosClient {

    // ------------------- USUARIOS -------------------

    /**
     * Obtiene la lista completa de usuarios.
     *
     * @return Lista de UsuarioDTO
     */
    @GetMapping("/usuarios")
    List<UsuarioDTO> listarUsuarios();

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario
     * @return UsuarioDTO correspondiente
     */
    @GetMapping("/usuarios/{id}")
    UsuarioDTO findById(@PathVariable("id") Long id);

    /**
     * Crea un nuevo usuario.
     *
     * @param dto Datos del usuario a crear
     * @return UsuarioDTO creado
     */
    @PostMapping("/usuarios")
    UsuarioDTO crearUsuario(@RequestBody UsuarioRequestDTO dto);

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param id ID del usuario a actualizar
     * @param usuarioDTO Datos actualizados del usuario
     * @return UsuarioDTO actualizado
     */
    @PutMapping("/usuarios/{id}")
    UsuarioDTO actualizar(@PathVariable("id") Long id, @RequestBody UsuarioDTO usuarioDTO);

    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param correo Correo del usuario
     * @return UsuarioDTO correspondiente
     */
    @GetMapping("/usuarios/correo/{correo}")
    UsuarioDTO buscarPorCorreo(@PathVariable("correo") String correo);

    // ------------------- LOGIN -------------------

    /**
     * Realiza el login de un usuario.
     *
     * @param request Datos de login (correo y contraseña)
     * @return LoginResponseDTO con información de autenticación
     */
    @PostMapping("/usuarios/login")
    LoginResponseDTO login(@RequestBody LoginRequestDTO request);

    // ------------------- DOCTORES -------------------

    /**
     * Obtiene la lista completa de doctores.
     *
     * @return Lista de DoctorDTO
     */
    @GetMapping("/usuarios/doctores")
    List<DoctorDTO> listarDoctores();

    /**
     * Crea un nuevo doctor.
     *
     * @param dto Datos del doctor a crear
     * @return DoctorDTO creado
     */
    @PostMapping("/usuarios/doctores")
    DoctorDTO crearDoctor(@RequestBody DoctorRequestDTO dto);

    /**
     * Busca un doctor por su ID.
     *
     * @param id ID del doctor
     * @return DoctorDTO correspondiente
     */
    @GetMapping("/usuarios/doctores/{id}")
    DoctorDTO buscarDoctorPorId(@PathVariable("id") Long id);

    /**
     * Busca un doctor por el ID del usuario asociado.
     *
     * @param usuarioId ID del usuario
     * @return DoctorDTO correspondiente
     */
    @GetMapping("/usuarios/doctores/usuario/{usuarioId}")
    DoctorDTO buscarDoctorPorUsuarioId(@PathVariable("usuarioId") Long usuarioId);

    /**
     * Elimina un doctor por su ID.
     *
     * @param id ID del doctor
     */
    @DeleteMapping("/usuarios/doctores/{id}")
    void eliminarDoctor(@PathVariable("id") Long id);

    /**
     * Busca doctores por su especialidad.
     *
     * @param especialidad Especialidad del doctor
     * @return Lista de DoctorDTO
     */
    @GetMapping("/usuarios/doctores/especialidad/{especialidad}")
    List<DoctorDTO> buscarDoctoresPorEspecialidad(@PathVariable("especialidad") String especialidad);

    // ------------------- PACIENTES -------------------

    /**
     * Obtiene la lista completa de pacientes.
     *
     * @return Lista de PacienteDTO
     */
    @GetMapping("/usuarios/pacientes")
    List<PacienteDTO> listarPacientes();

    /**
     * Crea un nuevo paciente.
     *
     * @param dto Datos del paciente a crear
     * @return PacienteDTO creado
     */
    @PostMapping("/usuarios/pacientes")
    PacienteDTO crearPaciente(@RequestBody PacienteRequestDTO dto);

    /**
     * Busca un paciente por su ID.
     *
     * @param id ID del paciente
     * @return PacienteDTO correspondiente
     */
    @GetMapping("/usuarios/pacientes/{id}")
    PacienteDTO buscarPacientePorId(@PathVariable("id") Long id);

    /**
     * Elimina un paciente por su ID.
     *
     * @param id ID del paciente
     */
    @DeleteMapping("/usuarios/pacientes/{id}")
    void eliminarPaciente(@PathVariable("id") Long id);
}
