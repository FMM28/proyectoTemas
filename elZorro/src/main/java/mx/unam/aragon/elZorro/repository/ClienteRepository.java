package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity,Long> {
    Optional<ClienteEntity> findByCorreo(String correo);

    Optional<ClienteEntity> findByTelefono(String telefono);
}
