package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.DetalleVentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity,Long> {
    @Query(value = "SELECT * FROM detalle_venta WHERE venta_id = :ventaId", nativeQuery = true)
    List<DetalleVentaEntity> findByVenta(@Param("ventaId") Long ventaId);


}
