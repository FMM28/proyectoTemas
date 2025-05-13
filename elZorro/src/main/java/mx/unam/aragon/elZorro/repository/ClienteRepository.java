package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity,Long> {
}
