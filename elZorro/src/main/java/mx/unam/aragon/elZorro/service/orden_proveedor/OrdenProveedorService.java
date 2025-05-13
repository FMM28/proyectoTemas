package mx.unam.aragon.elZorro.service.orden_proveedor;

import mx.unam.aragon.elZorro.model.entity.OrdenProveedorEntity;

import java.util.List;

public interface OrdenProveedorService {
    OrdenProveedorEntity save(OrdenProveedorEntity ordenProveedor);
    List<OrdenProveedorEntity> findAll();
    void deleteById(Long id);
    OrdenProveedorEntity findById(Long id);
}
