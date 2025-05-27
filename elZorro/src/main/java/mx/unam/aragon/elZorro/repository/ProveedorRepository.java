package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Long> {

    // Búsquedas exactas (se mantienen)
    Optional<ProveedorEntity> findByRfc(String rfc);
    Optional<ProveedorEntity> findByCorreo(String correo);
    Optional<ProveedorEntity> findByRazonSocialContainingIgnoreCase(String razonSocial);

    // Búsqueda combinada con múltiples criterios (sin filtro de estatus)
    @Query("SELECT DISTINCT p FROM proveedor p WHERE " +
            "(:rfc IS NULL OR :rfc = '' OR LOWER(p.rfc) LIKE LOWER(CONCAT('%', :rfc, '%'))) AND " +
            "(:razonSocial IS NULL OR :razonSocial = '' OR LOWER(p.razonSocial) LIKE LOWER(CONCAT('%', :razonSocial, '%'))) AND " +
            "(:correo IS NULL OR :correo = '' OR LOWER(p.correo) LIKE LOWER(CONCAT('%', :correo, '%'))) " +
            "ORDER BY p.razonSocial")
    List<ProveedorEntity> buscarProveedoresPorCriterios(
            @Param("rfc") String rfc,
            @Param("razonSocial") String razonSocial,
            @Param("correo") String correo);

    // Consultas para sugerencias (sin filtro de estatus)
    @Query(value = "SELECT p FROM proveedor p WHERE " +
            "LOWER(p.rfc) LIKE LOWER(CONCAT('%', :termino, '%')) " +
            "ORDER BY p.rfc")
    List<ProveedorEntity> findSugerenciasRfcSinLimite(@Param("termino") String termino);

    @Query(value = "SELECT p FROM proveedor p WHERE " +
            "LOWER(p.razonSocial) LIKE LOWER(CONCAT('%', :termino, '%')) " +
            "ORDER BY p.razonSocial")
    List<ProveedorEntity> findSugerenciasRazonSocialSinLimite(@Param("termino") String termino);

    @Query(value = "SELECT p FROM proveedor p WHERE " +
            "LOWER(p.correo) LIKE LOWER(CONCAT('%', :termino, '%')) " +
            "ORDER BY p.correo")
    List<ProveedorEntity> findSugerenciasCorreoSinLimite(@Param("termino") String termino);

    // Métodos originales (sin filtro de estatus)
    @Query("SELECT p FROM proveedor p WHERE LOWER(p.rfc) LIKE LOWER(CONCAT('%', :termino, '%')) ORDER BY p.rfc")
    List<ProveedorEntity> findSugerenciasRfc(@Param("termino") String termino);

    @Query("SELECT p FROM proveedor p WHERE LOWER(p.razonSocial) LIKE LOWER(CONCAT('%', :termino, '%')) ORDER BY p.razonSocial")
    List<ProveedorEntity> findSugerenciasRazonSocial(@Param("termino") String termino);

    @Query("SELECT p FROM proveedor p WHERE LOWER(p.correo) LIKE LOWER(CONCAT('%', :correo, '%')) ORDER BY p.correo")
    List<ProveedorEntity> findSugerenciasCorreo(@Param("correo") String correo);
}