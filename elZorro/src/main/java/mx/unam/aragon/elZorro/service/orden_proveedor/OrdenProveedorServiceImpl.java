package mx.unam.aragon.elZorro.service.orden_proveedor;

import mx.unam.aragon.elZorro.model.entity.OrdenProveedorEntity;
import mx.unam.aragon.elZorro.repository.OrdenProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrdenProveedorServiceImpl implements OrdenProveedorService {
    @Autowired
    private OrdenProveedorRepository ordenProveedorRepository;

    @Override
    @Transactional
    public OrdenProveedorEntity save(OrdenProveedorEntity ordenProveedor) {
        return ordenProveedorRepository.save(ordenProveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenProveedorEntity> findAll() {
        return ordenProveedorRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ordenProveedorRepository.deleteById(id);
    }

    @Override
    public OrdenProveedorEntity findById(Long id) {
        Optional<OrdenProveedorEntity> ordenProveedor = ordenProveedorRepository.findById(id);
        return ordenProveedor.orElse(null);
    }
}
