package mx.unam.aragon.elZorro.controller.flask;

import mx.unam.aragon.elZorro.model.entity.CategoriaEntity;
import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import mx.unam.aragon.elZorro.model.json.Inventario;
import mx.unam.aragon.elZorro.security.FlaskRequestSender;
import mx.unam.aragon.elZorro.service.inventario.InventarioService;
import mx.unam.aragon.elZorro.service.inventario.InventarioServiceImpl;
import mx.unam.aragon.elZorro.service.producto.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class FlaskController {
    @Autowired
    FlaskRequestSender requestSender;

    @Autowired
    InventarioServiceImpl inventarioService;

    @GetMapping("/descarga_inventario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> generaExcel() {
        List<Inventario> productos = inventarioService.getAll();

        // Verificar que la lista no esté vacía
        if(productos == null || productos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay productos en el inventario");
        }

        ResponseEntity<byte[]> response = requestSender.postToFlask("/api/generar_excel", productos);

        // Extraer nombre de archivo o usar uno por defecto
        String fileName = Optional.ofNullable(response.getHeaders().getContentDisposition())
                .map(ContentDisposition::getFilename)
                .orElse("inventario_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + ".xlsx");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(Objects.requireNonNullElse(
                        response.getHeaders().getContentType(),
                        MediaType.APPLICATION_OCTET_STREAM
                ))
                .contentLength(response.getBody().length)
                .body(new ByteArrayResource(response.getBody()));
    }
}
