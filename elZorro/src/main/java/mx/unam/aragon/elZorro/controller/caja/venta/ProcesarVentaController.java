package mx.unam.aragon.elZorro.controller.caja.venta;

import jakarta.servlet.http.HttpSession;
import mx.unam.aragon.elZorro.model.dto.venta.CarritoDTO;
import mx.unam.aragon.elZorro.model.entity.DetalleVentaEntity;
import mx.unam.aragon.elZorro.model.entity.VentaEntity;
import mx.unam.aragon.elZorro.service.venta.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/venta")
@PreAuthorize("hasRole('CAJA')")
public class ProcesarVentaController {

    @Autowired
    private VentaService ventaService;

    /**
     * Procesar la venta (confirmar y guardar)
     */
    @PostMapping("/procesar")
    public String procesarVenta(HttpSession session,
                                RedirectAttributes redirectAttributes,
                                SessionStatus sessionStatus) {
        try {
            // Obtener carrito de la sesión
            CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");

            if (carrito == null || carrito.estaVacio()) {
                redirectAttributes.addFlashAttribute("error",
                        "No hay productos en el carrito para procesar");
                return "redirect:/venta/nueva-venta";
            }

            // Validar carrito antes de procesar
            if (!ventaService.validarVenta(carrito)) {
                String errores = ventaService.obtenerErroresValidacion(carrito);
                redirectAttributes.addFlashAttribute("error",
                        "Error en la validación: " + errores);
                return "redirect:/venta/nueva-venta";
            }
            System.out.println(carrito);

            // Procesar la venta
            VentaEntity ventaProcesada = ventaService.procesarVenta(carrito);

            // Limpiar sesión después del procesamiento exitoso
            sessionStatus.setComplete();

            // Redirigir a venta exitosa con los datos
            redirectAttributes.addFlashAttribute("ventaExitosa", ventaProcesada);
            redirectAttributes.addFlashAttribute("mensaje",
                    "¡Venta procesada exitosamente!");

            return "redirect:/venta/venta-exitosa/" + ventaProcesada.getId();

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error de validación: " + e.getMessage());
            return "redirect:/venta/nueva-venta";

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error de stock: " + e.getMessage());
            return "redirect:/venta/nueva-venta";

        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error",
                    "Error al procesar la venta: " + e.getMessage());
            return "redirect:/venta/nueva-venta";
        }
    }

    /**
     * Validación previa antes de mostrar confirmación
     */
    @PostMapping("/validar-para-procesar")
    @ResponseBody
    public Map<String, Object> validarParaProcesar(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");

            if (carrito == null || carrito.estaVacio()) {
                response.put("valido", false);
                response.put("mensaje", "El carrito está vacío");
                return response;
            }

            // Usar el servicio para validar
            boolean esValido = ventaService.validarVenta(carrito);
            response.put("valido", esValido);

            if (!esValido) {
                String errores = ventaService.obtenerErroresValidacion(carrito);
                response.put("mensaje", errores);
            } else {
                response.put("mensaje", "Carrito válido para procesar");
            }

        } catch (Exception e) {
            response.put("valido", false);
            response.put("mensaje", "Error en validación: " + e.getMessage());
        }

        return response;
    }

    /**
     * Vista de venta exitosa
     */
    @GetMapping("/venta-exitosa/{ventaId}")
    public String ventaExitosa(@PathVariable Long ventaId, Model model) {
        try {
            VentaEntity venta = ventaService.findById(ventaId);

            if (venta == null) {
                model.addAttribute("error", "Venta no encontrada");
                return "redirect:/venta/nueva-venta";
            }

            List<DetalleVentaEntity> detalles = ventaService.obtenerDetallesPorVenta(ventaId);

            // Calcular el total multiplicando cantidad * precioUnitario y sumando todo
            BigDecimal total = detalles.stream()
                    .map(detalle -> BigDecimal.valueOf(detalle.getPrecioUnitario()).multiply(BigDecimal.valueOf(detalle.getCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("venta", venta);
            model.addAttribute("Mitotal", total);
            model.addAttribute("detalles", detalles);

            return "caja/venta/venta-exitosa";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los datos de la venta");
            return "redirect:/venta/nueva-venta";
        }
    }

    /**
     * Verificar stock en tiempo real antes de procesar
     */
    @PostMapping("/verificar-stock")
    @ResponseBody
    public Map<String, Object> verificarStock(HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            CarritoDTO carrito = (CarritoDTO) session.getAttribute("carrito");

            if (carrito == null || carrito.estaVacio()) {
                response.put("stockValido", false);
                response.put("mensaje", "Carrito vacío");
                return response;
            }

            boolean stockDisponible = ventaService.verificarStockDisponible(carrito);
            response.put("stockValido", stockDisponible);

            if (!stockDisponible) {
                response.put("mensaje", "Algunos productos no tienen stock suficiente");
            } else {
                response.put("mensaje", "Stock disponible para todos los productos");
            }

        } catch (Exception e) {
            response.put("stockValido", false);
            response.put("mensaje", "Error al verificar stock: " + e.getMessage());
        }

        return response;
    }
}