package com.eps.usuarios.models.enums;

/**
 * Enumeración que representa las diferentes especialidades médicas disponibles
 * dentro del sistema de usuarios de la EPS.
 *
 * <p>Este enum se utiliza para asignar o filtrar médicos según su área de
 * atención, permitiendo una categorización clara y homogénea dentro de la
 * plataforma.</p>
 *
 * <p><b>Especialidades disponibles:</b></p>
 * <ul>
 *     <li><b>MEDICINA_GENERAL</b>: Atención médica primaria y general.</li>
 *     <li><b>ODONTOLOGIA</b>: Servicios relacionados con salud bucal.</li>
 *     <li><b>PEDIATRIA</b>: Especialidad enfocada en la atención de niños.</li>
 *     <li><b>DERMATOLOGIA</b>: Tratamiento y diagnóstico de enfermedades de la piel.</li>
 * </ul>
 *
 * <p>Se puede extender fácilmente agregando nuevas especialidades según los
 * requerimientos del sistema.</p>
 */
public enum Especialidad {
    MEDICINA_GENERAL,
    ODONTOLOGIA,
    PEDIATRIA,
    DERMATOLOGIA
}
