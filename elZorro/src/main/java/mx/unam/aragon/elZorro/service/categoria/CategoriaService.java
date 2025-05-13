package mx.unam.aragon.elZorro.service.categoria;

import mx.unam.aragon.elZorro.model.entity.CategoriaEntity;

import java.util.List;

public interface CategoriaService {
    CategoriaEntity save(CategoriaEntity categoria);
    List<CategoriaEntity> findAll();
    void deleteById(Long id);
    CategoriaEntity findById(Long id);
}
