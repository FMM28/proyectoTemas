package mx.unam.aragon.elZorro.service.movimiento_producto;

import mx.unam.aragon.elZorro.model.entity.MovimientoProductoEntity;

import java.util.List;

public interface MovimientoProductoService {
    MovimientoProductoEntity save(MovimientoProductoEntity movimientoProducto);
    List<MovimientoProductoEntity> findAll();
    void deleteById(Long id);
    MovimientoProductoEntity findById(Long id);
}
