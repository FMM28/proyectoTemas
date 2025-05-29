package mx.unam.aragon.elZorro.service.empleado;

import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import mx.unam.aragon.elZorro.model.entity.RolEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmpleadoService {
    EmpleadoEntity save(EmpleadoEntity empleado);
    List<EmpleadoEntity> findAll();
    void deleteById(Long id);
    EmpleadoEntity findById(Long id);
    EmpleadoEntity crearEmpleado(String nombre, String apellidoPaterno, String apellidoMaterno, String usuario, String passwordPlano, RolEntity rol);
    Page<EmpleadoEntity> findAllPaginated(Pageable pageable);
    Page<EmpleadoEntity> buscarPorNombre(String nombre, Pageable pageable);
    EmpleadoEntity findByUsername(String username);
}
