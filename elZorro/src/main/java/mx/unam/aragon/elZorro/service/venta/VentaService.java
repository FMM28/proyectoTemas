package mx.unam.aragon.elZorro.service.venta;

import mx.unam.aragon.elZorro.model.entity.VentaEntity;

import java.util.List;

public interface VentaService {
    VentaEntity save(VentaEntity venta);
    List<VentaEntity> findAll();
    void deleteById(Long id);
    VentaEntity findById(Long id);
}
