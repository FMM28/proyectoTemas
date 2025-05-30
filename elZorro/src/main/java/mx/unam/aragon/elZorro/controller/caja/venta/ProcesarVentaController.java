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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/venta")
@SessionAttributes("carrito")
public class ProcesarVentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/procesar")
    public String procesarVenta(
            @ModelAttribute("carrito") CarritoDTO carrito,
            SessionStatus sessionStatus,
            RedirectAttributes redirectAttributes,
            HttpSession session) { // Añade HttpSession

        try {
            // Validación más robusta
            if (carrito == null || carrito.estaVacio()) {
                redirectAttributes.addFlashAttribute("error", "El carrito está vacío o no se ha inicializado");
                return "redirect:/venta/nueva-venta";
            }

            // Asegurar que detalles no sea null
            if (carrito.getDetalles() == null) {
                carrito.setDetalles(new ArrayList<>());
            }

            if (!ventaService.validarVenta(carrito)) {
                String errores = ventaService.obtenerErroresValidacion(carrito);
                redirectAttributes.addFlashAttribute("error", errores);
                return "redirect:/venta/nueva-venta";
            }

            System.out.println(carrito);

            VentaEntity ventaProcesada = ventaService.procesarVenta(carrito);

            // Limpiar la sesión completamente
            sessionStatus.setComplete();
            session.removeAttribute("carrito");

            redirectAttributes.addFlashAttribute("ventaExitosa", ventaProcesada);
            return "redirect:/venta/venta-exitosa/" + ventaProcesada.getId();

        } catch (Exception e) {
            e.printStackTrace(); // Mejor logging
            redirectAttributes.addFlashAttribute("error",
                    "Error al procesar la venta: " + e.getMessage());
            return "redirect:/venta/nueva-venta";
        }
    }


    @GetMapping("/venta-exitosa/{ventaId}")
    public String ventaExitosa(@PathVariable Long ventaId, Model model) {
        VentaEntity venta = ventaService.findById(ventaId);
        if (venta == null) {
            return "redirect:/venta/nueva-venta";
        }

        List<DetalleVentaEntity> detalles = ventaService.obtenerDetallesPorVenta(ventaId);
        BigDecimal total = calcularTotal(detalles);

        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);
        model.addAttribute("total", total);

        // Cambia a esto (sin especificar parámetros en el fragmento)
        model.addAttribute("mainContent", "caja/venta/venta-exitosa");

        return "common/layout";
    }

    private BigDecimal calcularTotal(List<DetalleVentaEntity> detalles) {
        return detalles.stream()
                .map(d -> BigDecimal.valueOf(d.getPrecioUnitario()).multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}