package mx.unam.aragon.elZorro.controller.caja.venta;

import jakarta.servlet.http.HttpSession;
import mx.unam.aragon.elZorro.model.dto.venta.CarritoDTO;
import mx.unam.aragon.elZorro.model.dto.venta.ProductoCarritoDTO;
import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.service.cliente.ClienteService;
import mx.unam.aragon.elZorro.service.empleado.EmpleadoService;
import mx.unam.aragon.elZorro.service.metodo_pago.MetodoPagoService;
import mx.unam.aragon.elZorro.service.producto.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/venta")
@SessionAttributes("carrito")
@PreAuthorize("hasRole('CAJA')")
public class NuevaVentaController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private MetodoPagoService metodoPagoService;
    @Autowired
    private CarritoController carritoController;

    @ModelAttribute("carrito")
    public CarritoDTO carrito() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Long empleadoId = empleadoService.findByUsername(username).getId(); // Método hipotético
        return new CarritoDTO(empleadoId);
    }

    @GetMapping("/nueva-venta")
    public String mostrarVenta(
            @ModelAttribute("carrito") CarritoDTO carrito,
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String categoria,
            Model model) {
        if (carrito.getDetalles() == null) {
            carrito.setDetalles(new ArrayList<>());
        }
        // Cargar datos necesarios para la vista
        cargarDatosVista(model, carrito, busqueda, categoria);
        model.addAttribute("mainContent", "caja/venta/nueva-venta");

        return "common/layout";
    }

    @PostMapping("/establecer-cliente")
    public String establecerCliente(
            @ModelAttribute("carrito") CarritoDTO carrito,
            @RequestParam Long clienteId,
            RedirectAttributes redirectAttributes) {

        clienteService.findById(clienteId).ifPresent(cliente -> {
            carrito.getVentaInfo().establecerCliente(
                    clienteId,
                    cliente.getNombre() + cliente.getApellidoPaterno() + cliente.getApellidoMaterno()
            );
        });

        redirectAttributes.addFlashAttribute("mensaje", "Cliente establecido correctamente");
        return "redirect:/venta/nueva-venta";
    }

    @PostMapping("/establecer-metodo-pago")
    public String establecerMetodoPago(
            @ModelAttribute("carrito") CarritoDTO carrito,
            @RequestParam(required = false) Long metodoPagoId, // Hacerlo opcional
            RedirectAttributes redirectAttributes) {

        if (metodoPagoId == null) {
            redirectAttributes.addFlashAttribute("error", "Debe seleccionar un método de pago válido");
            return "redirect:/venta/nueva-venta";
        }

        var metodoPago = metodoPagoService.findById(metodoPagoId);
        if (metodoPago != null) {
            carrito.getVentaInfo().establecerMetodoPago(
                    metodoPagoId,
                    metodoPago.getNombre()
            );
            redirectAttributes.addFlashAttribute("mensaje", "Método de pago establecido");
        } else {
            redirectAttributes.addFlashAttribute("error", "Método de pago no encontrado");
        }

        return "redirect:/venta/nueva-venta";
    }

    @PostMapping("/limpiar-carrito")
    public String limpiarCarrito(
            @ModelAttribute("carrito") CarritoDTO carrito,
            SessionStatus status) {

        carrito.limpiar();
        status.setComplete();
        return "redirect:/venta/nueva-venta";
    }

    @PostMapping("/cancelar")
    public String cancelarVenta(SessionStatus status) {
        status.setComplete();
        return "redirect:/venta/nueva-venta";
    }

    private void cargarDatosVista(Model model, CarritoDTO carrito, String busqueda, String categoria) {
        // Productos según búsqueda/categoría
        List<ProductoEntity> productos = obtenerProductosFiltrados(busqueda, categoria);
        List<ProductoCarritoDTO> productosCarrito = convertirADTO(productos);

        model.addAttribute("carrito", carrito);
        model.addAttribute("productos", productosCarrito);
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("metodosPago", metodoPagoService.findAll());
        model.addAttribute("empleados", empleadoService.findAll());
    }

    private List<ProductoEntity> obtenerProductosFiltrados(String busqueda, String categoria) {
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            return productoService.buscarPorNombre(busqueda);
        } else if (categoria != null && !categoria.trim().isEmpty()) {
            return productoService.buscarPorCategoria(categoria);
        }
        return productoService.findAll();
    }

    private List<ProductoCarritoDTO> convertirADTO(List<ProductoEntity> productos) {
        return productos.stream()
                .map(ProductoCarritoDTO::new)
                .collect(Collectors.toList());
    }
}