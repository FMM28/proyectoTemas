package mx.unam.aragon.elZorro.service.cliente;

import mx.unam.aragon.elZorro.model.entity.ClienteEntity;

import java.util.List;

public interface ClienteService {
    ClienteEntity save(ClienteEntity cliente);
    List<ClienteEntity> findAll();
    void deleteById(Long id);
    ClienteEntity findById(Long id);
    long count();
    boolean existsById(Long id);
}
