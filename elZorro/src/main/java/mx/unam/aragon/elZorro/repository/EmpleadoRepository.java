package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity,Long> {
    @Query(value = "SELECT a FROM empleado a WHERE a.usuario = ?1")
    EmpleadoEntity findByUsuario(String usuario);
}
