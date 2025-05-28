package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.model.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VentaRepository extends JpaRepository<VentaEntity,Long> {

    @Query(value = """
        SELECT p.*
        FROM producto p
        JOIN detalle_venta dv ON p.producto_id = dv.producto_id
        WHERE dv.venta_id = ?1
    """, nativeQuery = true)
    List<ProductoEntity> obtenerProductosPorVentaId(Long ventaId);
}
