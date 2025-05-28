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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

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

    /**
     * Mostrar la vista principal de nueva venta
     */
    @GetMapping("/nueva-venta")
    public String mostrarNuevaVenta(Model model, HttpSession session) {
        // Inicializar carrito si no existe
        CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
        if (carrito == null) {
            // Por defecto, usar empleado ID 1 (esto debería venir del usuario logueado)
            carrito = new CarritoDTO(1L);
            session.setAttribute("carrito", carrito);
        }

        // Cargar datos necesarios para la vista
        model.addAttribute("carrito", carrito);
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("metodosPago", metodoPagoService.findAll());
        model.addAttribute("empleados", empleadoService.findAll());

        return "caja/venta/nueva-venta";
    }

    /**
     * Fragment: Buscar productos
     */
    @GetMapping("/fragments/buscar-productos")
    public String buscarProductos(@RequestParam(required = false) String busqueda,
                                  @RequestParam(required = false) String categoria,
                                  Model model) {
        List<ProductoEntity> productos;

        if (busqueda != null && !busqueda.trim().isEmpty()) {
            productos = productoService.buscarPorNombre(busqueda);
        } else if (categoria != null && !categoria.trim().isEmpty()) {
            productos = productoService.buscarPorCategoria(categoria);
        } else {
            productos = productoService.findAll();
        }

        // Convertir a ProductoCarritoDTO para la vista
        List<ProductoCarritoDTO> productosCarrito = productos.stream()
                .map(ProductoCarritoDTO::new)
                .collect(Collectors.toList());

        model.addAttribute("productos", productosCarrito);
        return "caja/venta/fragments/buscar-productos :: productos-lista";
    }

    /**
     * Fragment: Items del carrito
     */
    @GetMapping("/fragments/carrito-items")
    public String carritoItems(Model model, HttpSession session) {
        CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new CarritoDTO(1L);
            session.setAttribute("carrito", carrito);
        }

        model.addAttribute("carrito", carrito);
        return "caja/venta/fragments/carrito-items :: carrito-contenido";
    }

    /**
     * Fragment: Seleccionar cliente
     */
    @GetMapping("/fragments/seleccionar-cliente")
    public String seleccionarCliente(Model model) {
        model.addAttribute("clientes", clienteService.findAll());
        return "fragments/seleccionar-cliente :: clientes-lista";
    }

    /**
     * Fragment: Confirmar venta
     */
    @GetMapping("/fragments/confirmar-venta")
    public String confirmarVenta(Model model, HttpSession session) {
        CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
        if (carrito == null || carrito.estaVacio()) {
            model.addAttribute("error", "El carrito está vacío");
            return "fragments/confirmar-venta :: confirmacion-error";
        }

        model.addAttribute("carrito", carrito);
        model.addAttribute("metodosPago", metodoPagoService.findAll());
        return "caja/venta/fragments/confirmar-venta :: confirmacion-contenido";
    }

    /**
     * Establecer cliente para la venta
     */
    @PostMapping("/establecer-cliente")
    @ResponseBody
    public String establecerCliente(@RequestParam Long clienteId,
                                    HttpSession session) {
        try {
            CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
            if (carrito == null) {
                return "error:Carrito no encontrado";
            }

            var cliente = clienteService.findById(clienteId);
            if (cliente.isPresent()) {
                carrito.getVentaInfo().establecerCliente(
                        clienteId,
                        cliente.get().getNombre() + " " + cliente.get().getApellidoPaterno() + " " + cliente.get().getApellidoMaterno()
                );
                session.setAttribute("carrito", carrito);
                return "success:Cliente establecido correctamente";
            } else {
                return "error:Cliente no encontrado";
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }

    /**
     * Establecer método de pago
     */
    @PostMapping("/establecer-metodo-pago")
    @ResponseBody
    public String establecerMetodoPago(@RequestParam Long metodoPagoId,
                                       HttpSession session) {
        try {
            CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
            if (carrito == null) {
                return "error:Carrito no encontrado";
            }

            var metodoPago = metodoPagoService.findById(metodoPagoId);
            if (metodoPago != null) {
                carrito.getVentaInfo().establecerMetodoPago(
                        metodoPagoId,
                        metodoPago.getNombre()
                );
                session.setAttribute("carrito", carrito);
                return "success:Método de pago establecido correctamente";
            } else {
                return "error:Método de pago no encontrado";
            }
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }

    /**
     * Limpiar carrito y empezar nueva venta
     */
    @PostMapping("/limpiar-carrito")
    public String limpiarCarrito(HttpSession session, SessionStatus status) {
        CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
        if (carrito != null) {
            carrito.limpiar();
            session.setAttribute("carrito", carrito);
        }

        return "redirect:/venta/nueva-venta";
    }

    /**
     * Cancelar venta y limpiar sesión
     */
    @PostMapping("/cancelar")
    public String cancelarVenta(SessionStatus status) {
        status.setComplete(); // Limpia todos los atributos de sesión
        return "redirect:/venta/nueva-venta";
    }
}