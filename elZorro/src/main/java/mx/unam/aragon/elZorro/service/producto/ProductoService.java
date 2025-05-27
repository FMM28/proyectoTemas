package mx.unam.aragon.elZorro.service.producto;

import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductoService {
    ProductoEntity save(ProductoEntity producto);
    List<ProductoEntity> findAll();
    Page<ProductoEntity> findAllPaginated(Pageable pageable);
    Page<ProductoEntity> buscarPorNombre(String nombre, Pageable pageable);
    ProductoEntity findById(Long id);
    void deleteById(Long id);
}
