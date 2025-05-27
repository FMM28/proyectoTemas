package mx.unam.aragon.elZorro.service.inventario;

import mx.unam.aragon.elZorro.model.json.Inventario;
import mx.unam.aragon.elZorro.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {
    @Autowired
    ProductoRepository productoRepository;

    @Override
    public List<Inventario> getAll() {
        return productoRepository.getInventario();
    }
}
