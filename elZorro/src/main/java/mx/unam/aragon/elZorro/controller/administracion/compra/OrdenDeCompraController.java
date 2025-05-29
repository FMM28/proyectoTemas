package mx.unam.aragon.elZorro.controller.administracion.compra;

import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import mx.unam.aragon.elZorro.model.entity.OrdenProveedorEntity;
import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import mx.unam.aragon.elZorro.service.email.EmailService;
import mx.unam.aragon.elZorro.service.empleado.EmpleadoService;
import mx.unam.aragon.elZorro.service.orden_proveedor.OrdenProveedorService;
import mx.unam.aragon.elZorro.service.producto.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/administracion/compras")
@PreAuthorize("hasRole('ADMIN')")
public class OrdenDeCompraController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrdenProveedorService ordenProveedorService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private EmpleadoService empleadoService;


    @PostMapping("/solicitar-pedido")
    public String solicitarPedido(
            @RequestParam Long productoId,
            @RequestParam Integer cantidad,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {  // Añadido parámetro Authentication

        // Obtener el producto y su proveedor
        ProductoEntity producto = productoService.findById(productoId);
        ProveedorEntity proveedor = producto.getProveedor();

        // Obtener el usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Obtener el empleado asociado a este usuario
        EmpleadoEntity empleado = empleadoService.findByUsername(username);


        // Crear el mensaje del correo
        String asunto = "Solicitud de pedido - Producto: " + producto.getNombre();
        String mensaje = String.format(
                "Se le solicita el siguiente pedido:\n\n" +
                        "Producto: %s\n" +
                        "Cantidad: %d\n" +
                        "Solicitado por: %s\n\n" +
                        "Por favor confirmar disponibilidad y fecha de entrega.",
                producto.getNombre(), cantidad, producto.getPrecio(), empleado.getNombre(), " ", empleado.getApellidoPaterno()
        );

        // Enviar el correo
        emailService.sendSimpleEmail(
                proveedor.getCorreo(),
                asunto,
                mensaje
        );

        // Crear y guardar la orden
        OrdenProveedorEntity orden = OrdenProveedorEntity.builder()
                .producto(producto)
                .proveedor(proveedor)
                .cantidad(cantidad)
                .fecha(LocalDate.now())
                .empleado(empleado)
                .build();

        ordenProveedorService.save(orden);

        redirectAttributes.addFlashAttribute("success", "Solicitud de pedido enviada al proveedor correctamente");
        return "redirect:/administracion/inventario/producto/lista";
    }
}