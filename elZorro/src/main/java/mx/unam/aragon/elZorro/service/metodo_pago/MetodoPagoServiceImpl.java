package mx.unam.aragon.elZorro.service.metodo_pago;

import mx.unam.aragon.elZorro.model.entity.MetodoPagoEntity;
import mx.unam.aragon.elZorro.repository.MetodoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MetodoPagoServiceImpl implements MetodoPagoService {
    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    @Override
    @Transactional
    public MetodoPagoEntity save(MetodoPagoEntity metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPagoEntity> findAll() {
        return metodoPagoRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        metodoPagoRepository.deleteById(id);
    }

    @Override
    public MetodoPagoEntity findById(Long id) {
        Optional<MetodoPagoEntity> metodoPago = metodoPagoRepository.findById(id);
        return metodoPago.orElse(null);
    }

    @Override
    public MetodoPagoEntity crearMetodo(String nombre){
        MetodoPagoEntity metodoPago = MetodoPagoEntity.builder().nombre(nombre).build();
        return metodoPagoRepository.save(metodoPago);
    }
}
