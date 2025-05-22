package mx.unam.aragon.elZorro.service.cliente;

import mx.unam.aragon.elZorro.model.entity.ClienteEntity;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    ClienteEntity save(ClienteEntity cliente);
    List<ClienteEntity> findAll();
    void deleteById(Long id);
    Optional<ClienteEntity> findById(Long id);
    long count();
    boolean existsById(Long id);
    Optional<ClienteEntity> findByCorreo(String correo);
    Optional<ClienteEntity> findByTelefono(String telefono);

}
