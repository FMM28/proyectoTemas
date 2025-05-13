package mx.unam.aragon.elZorro.service.detalle_venta;

import mx.unam.aragon.elZorro.model.entity.DetalleVentaEntity;
import mx.unam.aragon.elZorro.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Override
    @Transactional
    public DetalleVentaEntity save(DetalleVentaEntity detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVentaEntity> findAll() {
        return detalleVentaRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        detalleVentaRepository.deleteById(id);
    }

    @Override
    public DetalleVentaEntity findById(Long id) {
        Optional<DetalleVentaEntity> detalleVenta = detalleVentaRepository.findById(id);
        return detalleVenta.orElse(null);
    }
}
