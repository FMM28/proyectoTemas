package mx.unam.aragon.elZorro.validator;

import mx.unam.aragon.elZorro.model.entity.ClienteEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ClienteValidator implements Validator {
    private static final String CELULAR_PATTERN = "^\\+?[0-9 ]+$";
    private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean supports(Class<?> clazz) {
        return ClienteEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ClienteEntity cliente = (ClienteEntity) target;
        if(cliente.getNombre() == null || cliente.getNombre().isEmpty()){
            errors.rejectValue("nombre", "cliente.nombre.vacio", "El nombre no puede estar vacio");
        }
        if (cliente.getApellidoPaterno()==null || cliente.getApellidoPaterno().isEmpty()){
            errors.rejectValue("apellidoPaterno","cliente.apellidoPaterno.vacio", "El apellido paterno no puede estar vacio");
        }
        if (cliente.getApellidoMaterno()==null || cliente.getApellidoMaterno().isEmpty()){
            errors.rejectValue("apellidoMaterno","cliente.apellidoMaterno.vacio", "El apellido materno no puede estar vacio");
        }
        if (cliente.getTelefono()==null || cliente.getTelefono().isEmpty()){
            errors.rejectValue("telefono","cliente.telefono.vacio", "El telefono no puede estar vacio");
        } else if (!cliente.getTelefono().matches(CELULAR_PATTERN)) {
            errors.rejectValue("telefono","cliente.telefono.patron","El telefono no tiene el patron correcto");
        }
        if(cliente.getCorreo()==null || cliente.getCorreo().isEmpty()){
            errors.rejectValue("correo","cliente.correo.vacio", "El correo no puede estar vacio");
        } else if (!cliente.getCorreo().matches(EMAIL_PATTERN)) {
            errors.rejectValue("correo","cliente.correo.patron","El correo no tiene el patron correcto");
        }
    }
}
