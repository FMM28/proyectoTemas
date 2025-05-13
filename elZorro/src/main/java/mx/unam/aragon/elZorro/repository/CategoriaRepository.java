package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity,Long> {
}
