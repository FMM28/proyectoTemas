package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.model.json.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<ProductoEntity,Long> {
    @Query(value = """
    SELECT 
        p.producto_id as id,
        p.nombre as nombre,
        p.precio as precio,
        p.stock as stock,
        c.nombre as categoria,
        pv.razon_social as razonSocialProveedor,
        pv.rfc as rfc,
        pv.correo as correo
    FROM producto p 
    JOIN categoria c ON p.categoria_id = c.categoria_id
    JOIN proveedor pv ON p.proveedor_id = pv.proveedor_id
    """, nativeQuery = true)
    List<Inventario> getInventario();
}
