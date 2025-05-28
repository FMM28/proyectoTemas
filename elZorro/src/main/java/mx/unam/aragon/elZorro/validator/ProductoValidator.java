package mx.unam.aragon.elZorro.validator;

import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ProductoEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductoEntity producto = (ProductoEntity) target;
        if(producto.getNombre() == null || producto.getNombre().isEmpty()){
            errors.rejectValue("nombre", "producto.nombre.vacio", "El nombre no puede estar vacio");
        }
        if(producto.getPrecio() == null){
            errors.rejectValue("precio", "producto.precio.vacio", "Precio no puede estar vacio");
        }else {
            if(producto.getPrecio() < 0){
                errors.rejectValue("precio", "producto.precio.negativo", "Precio no puede ser negativo");
            }
            if(producto.getPrecio() == 0.0){
                errors.rejectValue("precio", "producto.precio.zero", "Precio no puede ser 0");
            }
        }
        if(producto.getImagen() == null || producto.getImagen().isEmpty()){
            errors.rejectValue("imagen", "producto.imagen.vacio", "Sin Imagen");
        }
        if(producto.getStock() < 0){
            errors.rejectValue("stock","producto.stock.negativo", "Stock no puede ser negativo");
        }
        if(producto.getCategoria() == null){
            errors.rejectValue("categoria","producto.categoria.vacio", "Categoria no puede ser vacio");
        }
        if(producto.getProveedor() == null){
            errors.rejectValue("proveedor","producto.proveedor.vacio", "Proveedor no puede estar vacio");
        }
    }
}
