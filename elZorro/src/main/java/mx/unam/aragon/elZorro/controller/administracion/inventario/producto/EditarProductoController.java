package mx.unam.aragon.elZorro.controller.administracion.inventario.producto;

import mx.unam.aragon.elZorro.converter.DoubleConverter;
import mx.unam.aragon.elZorro.converter.IntegerConverter;
import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.service.ImageStorageService;
import mx.unam.aragon.elZorro.service.categoria.CategoriaService;
import mx.unam.aragon.elZorro.service.producto.ProductoService;
import mx.unam.aragon.elZorro.service.proveedor.ProveedorService;
import mx.unam.aragon.elZorro.validator.ProductoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.util.Arrays;


@Controller
@RequestMapping("/administracion/inventario/producto")
@PreAuthorize("hasRole('ADMIN')")
public class EditarProductoController {
    @Autowired
    ProductoService productoService;
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ProveedorService proveedorService;
    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    private ProductoValidator productoValidator;

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        ProductoEntity producto = productoService.findById(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("proveedores", proveedorService.findAll());
        model.addAttribute("mainContent", "inventario/editar_producto");
        return "common/layout";
    }

    @PostMapping("/editar/{id}")
    public String procesarEdicionProducto(
            @PathVariable Long id,
            @Valid @ModelAttribute("producto") ProductoEntity producto,
            BindingResult result,
            @RequestParam("imagenFile") MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {

        // Validación de imagen
        if (!imagenFile.isEmpty()) {
            String contentType = imagenFile.getContentType();
            if (!Arrays.asList("image/jpeg", "image/png", "image/webp").contains(contentType)) {
                result.rejectValue("imagenFile", "error.imagen", "Formato de imagen no válido");
            } else if (imagenFile.getSize() > 2 * 1024 * 1024) {
                result.rejectValue("imagenFile", "error.imagen", "La imagen no puede superar 2MB");
            }
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.producto", result);
            redirectAttributes.addFlashAttribute("producto", producto);
            return "redirect:/administracion/inventario/producto/editar/" + id;
        }

        try {
            ProductoEntity productoExistente = productoService.findById(id);

            // Actualizar campos editables
            productoExistente.setNombre(producto.getNombre());
            productoExistente.setPrecio(producto.getPrecio());
            productoExistente.setCategoria(producto.getCategoria());
            productoExistente.setProveedor(producto.getProveedor());

            // Manejo de imagen
            if (!imagenFile.isEmpty()) {
                // Eliminar imagen anterior si existe
                if (productoExistente.getImagen() != null) {
                    imageStorageService.deleteImage(productoExistente.getImagen());
                }
                // Guardar nueva imagen
                String nombreImagen = imageStorageService.storeImage(imagenFile);
                productoExistente.setImagen(nombreImagen);
            }

            productoService.save(productoExistente);
            redirectAttributes.addFlashAttribute("success", "Producto actualizado exitosamente");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el producto");
        }

        return "redirect:/administracion/inventario/producto/lista";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productoService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto");
        }
        return "redirect:/administracion/inventario/producto/lista";
    }

    @InitBinder("producto")
    public void initProductoBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Double.class, "precio", new DoubleConverter());
        binder.registerCustomEditor(Integer.class,"stock",new IntegerConverter());
        binder.addValidators(productoValidator);
    }
}
