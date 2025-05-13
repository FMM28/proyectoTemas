package mx.unam.aragon.elZorro.service.producto;

import mx.unam.aragon.elZorro.model.entity.ProductoEntity;

import java.util.List;

public interface ProductoService {
    ProductoEntity save(ProductoEntity producto);
    List<ProductoEntity> findAll();
    void deleteById(Long id);
    ProductoEntity findById(Long id);
}
