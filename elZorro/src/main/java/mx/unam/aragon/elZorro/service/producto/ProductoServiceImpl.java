package mx.unam.aragon.elZorro.service.producto;

import lombok.SneakyThrows;
import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.repository.ProductoRepository;
import mx.unam.aragon.elZorro.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ImageStorageService imageStorageService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoEntity> findAll() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional
    public ProductoEntity save(ProductoEntity producto) {
        return productoRepository.save(producto);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void deleteById(Long id) {
        ProductoEntity producto = findById(id);
        if (producto != null && producto.getImagen() != null) {
            imageStorageService.deleteImage(producto.getImagen());
        }
        productoRepository.deleteById(id);
    }

    @Override
    public ProductoEntity findById(Long id) {
        Optional<ProductoEntity> producto = productoRepository.findById(id);
        return producto.orElse(null);
    }
}
