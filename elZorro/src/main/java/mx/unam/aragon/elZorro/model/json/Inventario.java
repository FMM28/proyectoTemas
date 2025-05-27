package mx.unam.aragon.elZorro.model.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Inventario(
        Integer id,
        String nombre,
        BigDecimal precio,
        Integer stock,
        String categoria,
        String razonSocialProveedor,
        String rfc,
        String correo
) { }
