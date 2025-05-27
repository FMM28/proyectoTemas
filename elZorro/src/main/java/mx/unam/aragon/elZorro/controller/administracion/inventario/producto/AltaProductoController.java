package mx.unam.aragon.elZorro.controller.administracion.inventario.producto;

import jakarta.validation.Valid;
import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.service.ImageStorageService;
import mx.unam.aragon.elZorro.service.categoria.CategoriaService;
import mx.unam.aragon.elZorro.service.producto.ProductoService;
import mx.unam.aragon.elZorro.service.proveedor.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;

@Controller
@RequestMapping("/administracion/inventario/producto")
@PreAuthorize("hasRole('ADMIN')")
public class AltaProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private ImageStorageService imageStorageService;

    @GetMapping("/alta")
    public String mostrarFormularioAlta(Model model) {
        if (!model.containsAttribute("producto")) {
            model.addAttribute("producto", new ProductoEntity());
        }

        // Agregar listas necesarias para los select
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("proveedores", proveedorService.findAll());

        model.addAttribute("mainContent", "inventario/alta_producto");
        return "common/layout";
    }

    @PostMapping("/alta")
    public String procesarAltaProducto(
            @Valid @ModelAttribute("producto") ProductoEntity producto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        // Validar la imagen
        if (producto.getImagenFile() != null && !producto.getImagenFile().isEmpty()) {
            String contentType = producto.getImagenFile().getContentType();
            if (!Arrays.asList("image/jpeg", "image/png", "image/webp").contains(contentType)) {
                result.rejectValue("imagenFile", "error.imagen", "Formato de imagen no válido");
            } else if (producto.getImagenFile().getSize() > 2 * 1024 * 1024) {
                result.rejectValue("imagenFile", "error.imagen", "La imagen no puede superar 2MB");
            }
        }

        if (result.hasErrors()) {
            // Si hay errores, mostrar el formulario nuevamente con los mensajes
            model.addAttribute("categorias", categoriaService.findAll());
            model.addAttribute("proveedores", proveedorService.findAll());
            model.addAttribute("mainContent", "inventario/alta_producto");
            return "common/layout";
        }

        try {
            // Procesar la imagen si existe
            if (producto.getImagenFile() != null && !producto.getImagenFile().isEmpty()) {
                String nombreImagen = imageStorageService.storeImage(producto.getImagenFile());
                producto.setImagen(nombreImagen);
            }

            productoService.save(producto);
            // Redirigir al listado con mensaje de éxito
            redirectAttributes.addFlashAttribute("success", "Producto registrado exitosamente");
            return "redirect:/administracion/inventario/producto/lista";
        } catch (IOException e) {
            model.addAttribute("error", "Error al guardar la imagen");
            model.addAttribute("categorias", categoriaService.findAll());
            model.addAttribute("proveedores", proveedorService.findAll());
            model.addAttribute("mainContent", "inventario/alta_producto");
            return "common/layout";
        }
    }
}