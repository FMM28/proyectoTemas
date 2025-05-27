package mx.unam.aragon.elZorro.service.detalle_venta;


import mx.unam.aragon.elZorro.model.entity.DetalleVentaEntity;

import java.util.List;

public interface DetalleVentaService {
    DetalleVentaEntity save(DetalleVentaEntity detalleVenta);
    List<DetalleVentaEntity> findAll();
    void deleteById(Long id);
    DetalleVentaEntity findById(Long id);
    List<DetalleVentaEntity> findByVenta(Long id);
}
