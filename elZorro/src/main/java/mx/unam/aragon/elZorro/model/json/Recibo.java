package mx.unam.aragon.elZorro.model.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Recibo(
    Long id,
    String vendedor,
    LocalDateTime fecha,
    String cliente,
    List<Producto> productos,
    Double total
) { }
