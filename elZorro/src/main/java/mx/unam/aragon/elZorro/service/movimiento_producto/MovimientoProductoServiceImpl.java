package mx.unam.aragon.elZorro.service.movimiento_producto;

import mx.unam.aragon.elZorro.model.entity.MovimientoProductoEntity;
import mx.unam.aragon.elZorro.repository.MovimientoProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MovimientoProductoServiceImpl implements MovimientoProductoService {
    @Autowired
    private MovimientoProductoRepository movimientoProductoRepository;

    @Override
    @Transactional
    public MovimientoProductoEntity save(MovimientoProductoEntity movimientoProducto) {
        return movimientoProductoRepository.save(movimientoProducto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoProductoEntity> findAll() {
        return movimientoProductoRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        movimientoProductoRepository.deleteById(id);
    }

    @Override
    public MovimientoProductoEntity findById(Long id) {
        Optional<MovimientoProductoEntity> movimientoProducto = movimientoProductoRepository.findById(id);
        return movimientoProducto.orElse(null);
    }
}
