package mx.unam.aragon.elZorro.service.metodo_pago;

import mx.unam.aragon.elZorro.model.entity.MetodoPagoEntity;

import java.util.List;

public interface MetodoPagoService {
    MetodoPagoEntity save(MetodoPagoEntity metodoPago);
    List<MetodoPagoEntity> findAll();
    void deleteById(Long id);
    MetodoPagoEntity findById(Long id);
}
