package mx.unam.aragon.elZorro.service.proveedor;

import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProveedorService {
    ProveedorEntity save(ProveedorEntity proveedor);
    List<ProveedorEntity> findAll();
    void deleteById(Long id);
    ProveedorEntity findById(Long id);
    ProveedorEntity findByCorreo(String correo);
    ProveedorEntity findByRfc(String rfc);
    ProveedorEntity findByRazonSocial(String razonSocial);

    @Transactional(readOnly = true)
    List<ProveedorEntity> findSugerenciasRfc(String termino, int limite);

    @Transactional(readOnly = true)
    List<ProveedorEntity> findSugerenciasRazonSocial(String termino, int limite);

    @Transactional(readOnly = true)
    List<ProveedorEntity> findSugerenciasCorreo(String termino, int limite);
}
