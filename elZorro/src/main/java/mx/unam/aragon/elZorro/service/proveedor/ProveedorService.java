package mx.unam.aragon.elZorro.service.proveedor;

import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import java.util.List;

public interface ProveedorService {

    // Métodos CRUD básicos
    ProveedorEntity save(ProveedorEntity proveedor);
    List<ProveedorEntity> findAll();
    void deleteById(Long id);
    ProveedorEntity findById(Long id);

    // Búsquedas individuales (existentes)
    ProveedorEntity findByRfc(String rfc);
    ProveedorEntity findByCorreo(String correo);
    ProveedorEntity findByRazonSocial(String razonSocial);

    // NUEVO: Búsqueda combinada
    List<ProveedorEntity> buscarProveedores(String rfc, String razonSocial, String correo);

    // Métodos para sugerencias
    List<ProveedorEntity> findSugerenciasRfc(String termino, int limite);
    List<ProveedorEntity> findSugerenciasRazonSocial(String termino, int limite);
    List<ProveedorEntity> findSugerenciasCorreo(String termino, int limite);
}