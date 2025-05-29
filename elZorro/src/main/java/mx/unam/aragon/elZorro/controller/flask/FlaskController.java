package mx.unam.aragon.elZorro.controller.flask;

import mx.unam.aragon.elZorro.model.entity.VentaEntity;
import mx.unam.aragon.elZorro.model.json.Inventario;
import mx.unam.aragon.elZorro.model.json.Recibo;
import mx.unam.aragon.elZorro.security.FlaskRequestSender;
import mx.unam.aragon.elZorro.service.email.EmailService;
import mx.unam.aragon.elZorro.service.inventario.InventarioServiceImpl;
import mx.unam.aragon.elZorro.service.recibo.ReciboService;
import mx.unam.aragon.elZorro.service.venta.VentaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class FlaskController {
    @Autowired
    FlaskRequestSender requestSender;
    @Autowired
    EmailService emailService;

    @Autowired
    InventarioServiceImpl inventarioService;
    @Autowired
    ReciboService reciboService;
    @Autowired
    VentaServiceImpl ventaService;

    @GetMapping("/descarga_inventario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> generaExcel() {
        List<Inventario> productos = inventarioService.getAll();

        if(productos == null || productos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No hay productos en el inventario");
        }

        ResponseEntity<byte[]> response = requestSender.postToFlask("/api/generar_excel", productos);

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

    @PostMapping(value = "/envia_recibo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','CAJA')")
    public String enviarRecibo(
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes) {  // Usamos RedirectAttributes para mensajes flash

        try {
            // Obtener los datos necesarios
            Recibo recibo = reciboService.getRecibo(id);
            VentaEntity venta = ventaService.findById(id);

            // Validar que existe el cliente y correo
            if (venta.getCliente() == null || venta.getCliente().getCorreo() == null) {
                redirectAttributes.addFlashAttribute("error", "El cliente no tiene correo electrónico registrado");
                return "redirect:/venta/venta-exitosa/" + id;
            }

            String correo = venta.getCliente().getCorreo();

            // Generar el PDF del recibo
            ResponseEntity<byte[]> response = requestSender.postToFlask("/api/generar_recibo", recibo);
            byte[] pdfBytes = response.getBody();

            // Obtener nombre del archivo
            String fileName = Optional.ofNullable(response.getHeaders().getContentDisposition())
                    .map(ContentDisposition::getFilename)
                    .orElse("Recibo_" + id + ".pdf");

            // Enviar el correo
            emailService.sendHtmlEmail(
                    correo,
                    "Recibo de Compra #" + id,
                    "",  // Puedes añadir un template HTML aquí
                    pdfBytes,
                    fileName
            );

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("success", "Recibo enviado exitosamente a " + correo);

        } catch (Exception e) {
            // Mensaje de error
            redirectAttributes.addFlashAttribute("error", "Error al enviar recibo: " + e.getMessage());
        }

        // Redireccionar a la página de venta exitosa
        return "redirect:/venta/venta-exitosa/" + id;
    }
}
