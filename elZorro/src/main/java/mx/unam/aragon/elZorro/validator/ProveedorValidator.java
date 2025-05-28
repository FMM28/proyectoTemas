package mx.unam.aragon.elZorro.validator;

import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProveedorValidator implements Validator {
    private static final String RFC_PATTERN_FISICA = "^[A-ZÑ&]{4}\\d{6}[A-Z0-9]{3}$";
    private static final String RFC_PATTERN_MORAL = "^[A-ZÑ&]{3}\\d{6}[A-Z0-9]{3}$";
    private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean supports(Class<?> clazz) {
        return ProveedorEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProveedorEntity proveedor = (ProveedorEntity) target;
        if(proveedor.getRazonSocial() == null || proveedor.getRazonSocial().trim().isEmpty()) {
            errors.rejectValue("razonSocial", "proveedor.razonSocial.vacio", "La razón social no puede estar vacía");
        }
        if(proveedor.getRfc() == null || proveedor.getRfc().trim().isEmpty()) {
            errors.rejectValue("rfc", "proveedor.rfc.vacio", "El RFC no puede estar vacío");
        } else {
            if (proveedor.getRfc().length() != 12 && proveedor.getRfc().length() != 13) {
                errors.rejectValue("rfc", "proveedor.rfc.longitud", "El RFC no tiene una longitud correcta");
            } else {
                boolean isPersonaFisica = proveedor.getRfc().length() == 13;
                String pattern = isPersonaFisica ? RFC_PATTERN_FISICA : RFC_PATTERN_MORAL;
                if (!proveedor.getRfc().matches(pattern)) {
                    errors.rejectValue("rfc", "proveedor.rfc.patron", "El RFC no tiene el patrón correcto");
                }
            }
        }
        if(proveedor.getDireccion() == null || proveedor.getDireccion().trim().isEmpty()) {
            errors.rejectValue("direccion", "proveedor.direccion.vacio", "La dirección no puede estar vacía");
        }
        if(proveedor.getCodigoPostal() == null || proveedor.getCodigoPostal().trim().isEmpty()) {
            errors.rejectValue("codigoPostal", "proveedor.codigoPostal.vacio", "El código postal no puede estar vacío");
        } else if (proveedor.getCodigoPostal().length() != 5) {
            errors.rejectValue("codigoPostal", "proveedor.codigoPostal.longitud", "El código postal debe tener 5 caracteres");
        }
        if(proveedor.getRegimenFiscal() == null) {
            errors.rejectValue("regimenFiscal", "proveedor.regimenFiscal.vacio", "El régimen fiscal no puede estar vacío");
        }
        if(proveedor.getCorreo() == null || proveedor.getCorreo().trim().isEmpty()) {
            errors.rejectValue("correo", "proveedor.correo.vacio", "El correo no puede estar vacío");
        } else if (!proveedor.getCorreo().matches(EMAIL_PATTERN)) {
            errors.rejectValue("correo", "proveedor.correo.invalido", "El correo no es válido");
        }
        if(proveedor.getTelefono() == null || proveedor.getTelefono().trim().isEmpty()) {
            errors.rejectValue("telefono", "proveedor.telefono.vacio", "El teléfono no puede estar vacío");
        }
        if(proveedor.getContactoNombre() == null || proveedor.getContactoNombre().trim().isEmpty()) {
            errors.rejectValue("contactoNombre", "proveedor.contactoNombre.vacio", "El nombre del contacto no puede estar vacío");
        }
    }
}