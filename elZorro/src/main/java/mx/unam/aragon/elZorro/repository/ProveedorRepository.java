package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<ProveedorEntity,Long> {
    Optional<ProveedorEntity> findByRfc(String rfc);

    Optional<ProveedorEntity> findByRazonSocialContainingIgnoreCase(String razonSocial);

    Optional<ProveedorEntity> findByCorreo(String correo);
}
