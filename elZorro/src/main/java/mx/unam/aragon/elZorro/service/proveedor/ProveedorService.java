package mx.unam.aragon.elZorro.service.proveedor;

import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;

import java.util.List;

public interface ProveedorService {
    ProveedorEntity save(ProveedorEntity proveedor);
    List<ProveedorEntity> findAll();
    void deleteById(Long id);
    ProveedorEntity findById(Long id);
    ProveedorEntity findByCorreo(String correo);
    ProveedorEntity findByRfc(String rfc);
    ProveedorEntity findByRazonSocialContainingIgnoreCase(String razonSocial);
}
