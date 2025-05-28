package mx.unam.aragon.elZorro.validator;

import mx.unam.aragon.elZorro.model.entity.CategoriaEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CategoriaValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CategoriaEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoriaEntity categoria = (CategoriaEntity) target;
        if (categoria.getNombre() == null || categoria.getNombre().isEmpty()) {
            errors.rejectValue("nombre", "error.categoria.nombre","La categoria no puede estar vacia");
        }
    }
}
