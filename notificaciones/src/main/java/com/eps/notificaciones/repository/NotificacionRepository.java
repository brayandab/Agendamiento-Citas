package com.eps.notificaciones.repository;

import com.eps.notificaciones.model.Notificacion;
import com.eps.notificaciones.model.NotificacionTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    List<Notificacion> findByPacienteId(Long pacienteId);

    List<Notificacion> findByCitaId(Long citaId);

    List<Notificacion> findByTipo(NotificacionTipo tipo);

    List<Notificacion> findByEstado(String estado);
}