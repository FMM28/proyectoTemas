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

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
@PreAuthorize("hasRole('CAJA')")
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    /**
     * Agregar producto al carrito
     */
    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarProducto(
            @RequestParam Long productoId,
            @RequestParam Integer cantidad,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Obtener carrito de la sesión
            CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
            if (carrito == null) {
                carrito = new CarritoDTO(1L); // ID por defecto del empleado
                session.setAttribute("carrito", carrito);
            }

            // Buscar el producto
            ProductoEntity producto = productoService.findById(productoId);
            if (producto == null) {
                response.put("success", false);
                response.put("message", "Producto no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            // Convertir a ProductoCarritoDTO
            ProductoCarritoDTO productoCarrito = new ProductoCarritoDTO(producto);

            // Agregar al carrito
            boolean agregado = carrito.agregarProducto(productoCarrito, cantidad);

            if (agregado) {
                session.setAttribute("carrito", carrito);
                response.put("success", true);
                response.put("message", "Producto agregado correctamente");
                response.put("totalItems", carrito.getCantidadTotalItems());
                response.put("total", carrito.getTotal());
                response.put("resumen", carrito.getResumen());
            } else {
                response.put("success", false);
                response.put("message", "No hay stock suficiente");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al agregar producto: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Modificar cantidad de un producto en el carrito
     */
    @PostMapping("/modificar-cantidad")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> modificarCantidad(
            @RequestParam Long productoId,
            @RequestParam Integer cantidad,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
            if (carrito == null) {
                response.put("success", false);
                response.put("message", "Carrito no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            boolean modificado = carrito.modificarCantidad(productoId, cantidad);

            if (modificado) {
                session.setAttribute("carrito", carrito);
                response.put("success", true);
                response.put("message", "Cantidad modificada correctamente");
                response.put("totalItems", carrito.getCantidadTotalItems());
                response.put("total", carrito.getTotal());
                response.put("resumen", carrito.getResumen());
            } else {
                response.put("success", false);
                response.put("message", "No se pudo modificar la cantidad. Verifique el stock disponible");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al modificar cantidad: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Eliminar producto del carrito
     */
    @PostMapping("/eliminar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarProducto(
            @RequestParam Long productoId,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
            if (carrito == null) {
                response.put("success", false);
                response.put("message", "Carrito no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            boolean eliminado = carrito.eliminarProducto(productoId);

            if (eliminado) {
                session.setAttribute("carrito", carrito);
                response.put("success", true);
                response.put("message", "Producto eliminado correctamente");
                response.put("totalItems", carrito.getCantidadTotalItems());
                response.put("total", carrito.getTotal());
                response.put("resumen", carrito.getResumen());
            } else {
                response.put("success", false);
                response.put("message", "No se pudo eliminar el producto");
            }

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar producto: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Obtener resumen del carrito (para actualizar UI)
     */
    @GetMapping("/resumen")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerResumen(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
            if (carrito == null) {
                carrito = new CarritoDTO(1L);
                session.setAttribute("carrito", carrito);
            }

            response.put("success", true);
            response.put("totalItems", carrito.getCantidadTotalItems());
            response.put("total", carrito.getTotal());
            response.put("resumen", carrito.getResumen());
            response.put("vacio", carrito.estaVacio());
            response.put("validoParaProcesar", carrito.esValidoParaProcesar());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener resumen: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Validar carrito antes de procesar
     */
    @GetMapping("/validar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validarCarrito(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
            if (carrito == null) {
                response.put("valido", false);
                response.put("errores", "Carrito no encontrado");
                return ResponseEntity.ok(response);
            }

            boolean valido = carrito.esValidoParaProcesar();
            response.put("valido", valido);

            if (!valido) {
                StringBuilder errores = new StringBuilder();

                if (carrito.estaVacio()) {
                    errores.append("El carrito está vacío. ");
                }

                if (carrito.getVentaInfo().getEmpleadoId() == null) {
                    errores.append("No se ha establecido el empleado. ");
                }

                if (carrito.getVentaInfo().getMetodoPagoId() == null) {
                    errores.append("No se ha establecido el método de pago. ");
                }

                response.put("errores", errores.toString());
            }

        } catch (Exception e) {
            response.put("valido", false);
            response.put("errores", "Error al validar carrito: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Obtener cantidad de un producto específico en el carrito
     */
    @GetMapping("/cantidad-producto")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerCantidadProducto(
            @RequestParam Long productoId,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");
            if (carrito == null) {
                response.put("cantidad", 0);
            } else {
                response.put("cantidad", carrito.getCantidadProducto(productoId));
            }

            response.put("success", true);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener cantidad: " + e.getMessage());
            response.put("cantidad", 0);
        }

        return ResponseEntity.ok(response);
    }
}