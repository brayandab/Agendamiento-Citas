/*
* Esta clase sirve para la comunicación con el microservicio correspondiente (Usuario) en donde
* se copia la url del microservicio y el nombre de este ( el nombre sale del properties, con el puerto correspondiente )
* si en el microservicio tiene diferentes clases lo recomendable es que la ruta del servicio empiece con el nombre
* del microservicio, y todas las rutas empuecen con ese nombre.
* Ejemplo:
* http://localhost:8080/usuarios
* http://localhost:8080/usuarios/pacientes
* http://localhost:8080/usuarios/doctores
*
* Esto para no tener que crear diferentes Client por cada endidad que se creo.
* */

package com.frontend.clients;

import com.frontend.dtos.UsuarioDTO;
import com.frontend.dtos.PacienteDTO;
import com.frontend.dtos.DoctorDTO;

import com.frontend.dtos.request.LoginRequestDTO;
import com.frontend.dtos.request.UsuarioRequestDTO;
import com.frontend.dtos.request.PacienteRequestDTO;
import com.frontend.dtos.request.DoctorRequestDTO;

import com.frontend.dtos.response.LoginResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "usuarios",
        url = "http://localhost:8081"
)
public interface UsuariosClient {

    // ----- USUARIOS -----
    @GetMapping("/usuarios")
    List<UsuarioDTO> listarUsuarios();

    @GetMapping("/usuarios/{id}")
    UsuarioDTO findById(@PathVariable Long id);

    @PostMapping("/usuarios")
    UsuarioDTO crearUsuario(@RequestBody UsuarioRequestDTO dto);

    @GetMapping("/usuarios/correo/{correo}")
    UsuarioDTO buscarPorCorreo(@PathVariable String correo);

    // ----- LOGIN -----
    @PostMapping("/usuarios/login")
    LoginResponseDTO login(@RequestBody LoginRequestDTO request);


    // ----- DOCTORES -----
    @GetMapping("/usuarios/doctores")
    List<DoctorDTO> listarDoctores();

    @PostMapping("/usuarios/doctores")
    DoctorDTO crearDoctor(@RequestBody DoctorRequestDTO dto);

    @GetMapping("/usuarios/doctores/{id}")
    DoctorDTO buscarDoctorPorId(@PathVariable Long id);

    @DeleteMapping("/usuarios/doctores/{id}")
    void eliminarDoctor(@PathVariable Long id);

    // ----- PACIENTES -----
    @GetMapping("/usuarios/pacientes")
    List<PacienteDTO> listarPacientes();

    @PostMapping("/usuarios/pacientes")
    PacienteDTO crearPaciente(@RequestBody PacienteRequestDTO dto);

    @GetMapping("/usuarios/pacientes/{id}")
    PacienteDTO buscarPacientePorId(@PathVariable Long id);

    @DeleteMapping("/usuarios/pacientes/{id}")
    void eliminarPaciente(@PathVariable Long id);
}
