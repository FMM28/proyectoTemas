package mx.unam.aragon.elZorro.service.rol;

import mx.unam.aragon.elZorro.model.entity.RolEntity;

import java.util.List;

public interface RolService {
    RolEntity save(RolEntity rol);
    List<RolEntity> findAll();
    void deleteById(Long id);
    RolEntity findById(Long id);
}
