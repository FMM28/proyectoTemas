package mx.unam.aragon.elZorro.service.producto;

import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductoService {
    ProductoEntity save(ProductoEntity producto) throws IOException;
    List<ProductoEntity> findAll();
    void deleteById(Long id);
    ProductoEntity findById(Long id);
}
