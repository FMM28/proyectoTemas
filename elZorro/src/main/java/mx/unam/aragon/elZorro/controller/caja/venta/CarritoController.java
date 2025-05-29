package mx.unam.aragon.elZorro.controller.caja.venta;

import jakarta.servlet.http.HttpSession;
import mx.unam.aragon.elZorro.model.dto.venta.CarritoDTO;
import mx.unam.aragon.elZorro.model.dto.venta.ProductoCarritoDTO;
import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.service.producto.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
@SessionAttributes("carrito") // Añadir esta anotación
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    // Asegurar que el carrito se inicialice si no existe
    @ModelAttribute("carrito")
    public CarritoDTO getCarrito() {
        return new CarritoDTO(1L); // 1L sería el ID del empleado (debería ser dinámico)
    }

    @PostMapping("/agregar")
    public String agregarProducto(
            @ModelAttribute("carrito") CarritoDTO carrito,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad,
            RedirectAttributes redirectAttributes,
            HttpSession session) { // Añadir HttpSession

        // Verificar inicialización
        if (carrito.getDetalles() == null) {
            carrito.setDetalles(new ArrayList<>());
        }

        ProductoEntity producto = productoService.findById(productoId);
        if (producto == null) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/venta/nueva-venta";
        }

        ProductoCarritoDTO productoCarrito = new ProductoCarritoDTO(producto);
        boolean agregado = carrito.agregarProducto(productoCarrito, cantidad);

        if (agregado) {
            // Actualizar el carrito en la sesión
            session.setAttribute("carrito", carrito);
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito");
        } else {
            redirectAttributes.addFlashAttribute("error", "No hay stock suficiente");
        }

        return "redirect:/venta/nueva-venta";
    }

    // Los demás métodos permanecen igual, pero añade HttpSession y verifica inicialización
    @PostMapping("/modificar-cantidad")
    public String modificarCantidad(
            @ModelAttribute("carrito") CarritoDTO carrito,
            @RequestParam Long productoId,
            @RequestParam Integer cantidad,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        if (carrito.getDetalles() == null) {
            redirectAttributes.addFlashAttribute("error", "Carrito no inicializado");
            return "redirect:/venta/nueva-venta";
        }

        boolean modificado = carrito.modificarCantidad(productoId, cantidad);

        if (modificado) {
            session.setAttribute("carrito", carrito);
            redirectAttributes.addFlashAttribute("mensaje", "Cantidad actualizada");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo modificar la cantidad");
        }

        return "redirect:/venta/nueva-venta";
    }

    @PostMapping("/eliminar")
    public String eliminarProducto(
            @ModelAttribute("carrito") CarritoDTO carrito,
            @RequestParam Long productoId,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        if (carrito.getDetalles() == null) {
            redirectAttributes.addFlashAttribute("error", "Carrito no inicializado");
            return "redirect:/venta/nueva-venta";
        }

        boolean eliminado = carrito.eliminarProducto(productoId);

        if (eliminado) {
            session.setAttribute("carrito", carrito);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el producto");
        }

        return "redirect:/venta/nueva-venta";
    }
}