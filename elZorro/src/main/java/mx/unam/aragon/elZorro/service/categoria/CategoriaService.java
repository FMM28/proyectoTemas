package mx.unam.aragon.elZorro.service.categoria;

import mx.unam.aragon.elZorro.model.entity.CategoriaEntity;

import java.util.List;

public interface CategoriaService {
    List<CategoriaEntity> findAll();
    CategoriaEntity findById(Long id);
    CategoriaEntity save(CategoriaEntity categoria);
    void deleteById(Long id);
}