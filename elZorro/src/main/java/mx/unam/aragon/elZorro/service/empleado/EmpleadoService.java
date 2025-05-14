package mx.unam.aragon.elZorro.service.empleado;

import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import mx.unam.aragon.elZorro.model.entity.RolEntity;

import java.util.List;

public interface EmpleadoService {
    EmpleadoEntity save(EmpleadoEntity empleado);
    List<EmpleadoEntity> findAll();
    void deleteById(Long id);
    EmpleadoEntity findById(Long id);
    EmpleadoEntity crearEmpleado(String usuario, String passwordPlano, RolEntity rol);
}
