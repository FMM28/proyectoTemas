package mx.unam.aragon.elZorro.validator;

import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmpleadoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EmpleadoEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmpleadoEntity empleado = (EmpleadoEntity) target;
        if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()) {
            errors.rejectValue("nombre", "empleado.nombre.vacio"    , "El nombre no puede estar vacio");
        }
        if(empleado.getApellidoPaterno() == null || empleado.getApellidoMaterno().trim().isEmpty()) {
            errors.rejectValue("apellidoPaterno", "empleado.apellidoPaterno.vacio", "El apellido paterno no puede estar vacio");
        }
        if(empleado.getApellidoMaterno() == null || empleado.getApellidoMaterno().trim().isEmpty()) {
            errors.rejectValue("apellidoMaterno","empleado.apellidoMaterno.vacio","El apellido materno no puede estar vacio");
        }
        if(empleado.getRol() == null || empleado.getRol().getNombre().isEmpty()) {
            errors.rejectValue("rol", "empleado.rol.vacio", "El rol no puede estar vacio");
        }
        if(empleado.getUsuario() == null || empleado.getUsuario().isEmpty()) {
            errors.rejectValue("usuario", "empleado.usuario.vacio", "El usuario no puede estar vacio");
        }
        if(empleado.getPassword() == null || empleado.getPassword().isEmpty()) {
            errors.rejectValue("password", "empleado.password.vacio", "El password no puede estar vacio");
        }
        if(empleado.getPassword().length() < 4) {
            errors.rejectValue("password","empleado.password.longitud","La contraseña es demasiado corta");
        }
    }
}
