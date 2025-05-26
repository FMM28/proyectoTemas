package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Long> {

    // Búsquedas exactas
    Optional<ProveedorEntity> findByRfc(String rfc);
    Optional<ProveedorEntity> findByCorreo(String correo);
    Optional<ProveedorEntity> findByRazonSocialContainingIgnoreCase(String razonSocial);

    // Consultas para sugerencias (ajustadas a los nombres de campos reales)
    @Query("SELECT p FROM proveedor p WHERE LOWER(p.rfc) LIKE LOWER(CONCAT('%', :termino, '%')) AND p.estatus = mx.unam.aragon.elZorro.model.enums.EstatusProveedor.ACTIVO ORDER BY p.rfc")
    List<ProveedorEntity> findSugerenciasRfc(@Param("termino") String termino);

    @Query("SELECT p FROM proveedor p WHERE LOWER(p.razonSocial) LIKE LOWER(CONCAT('%', :termino, '%')) AND p.estatus = mx.unam.aragon.elZorro.model.enums.EstatusProveedor.ACTIVO ORDER BY p.razonSocial")
    List<ProveedorEntity> findSugerenciasRazonSocial(@Param("termino") String termino);

    @Query("SELECT p FROM proveedor p WHERE LOWER(p.correo) LIKE LOWER(CONCAT('%', :termino, '%')) AND p.estatus = mx.unam.aragon.elZorro.model.enums.EstatusProveedor.ACTIVO ORDER BY p.correo")
    List<ProveedorEntity> findSugerenciasCorreo(@Param("termino") String termino);
}