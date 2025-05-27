package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoRepository extends JpaRepository<ProductoEntity,Long> {
    Page<ProductoEntity> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
