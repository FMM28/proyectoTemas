package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity,Long> {
}
