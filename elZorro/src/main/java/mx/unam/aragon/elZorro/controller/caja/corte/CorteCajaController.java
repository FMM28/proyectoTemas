package mx.unam.aragon.elZorro.controller.caja.corte;

import mx.unam.aragon.elZorro.model.entity.*;
import mx.unam.aragon.elZorro.service.cliente.ClienteService;
import mx.unam.aragon.elZorro.service.detalle_venta.DetalleVentaService;
import mx.unam.aragon.elZorro.service.empleado.EmpleadoService;
import mx.unam.aragon.elZorro.service.movimiento_producto.MovimientoProductoService;
import mx.unam.aragon.elZorro.service.producto.ProductoService;
import mx.unam.aragon.elZorro.service.venta.VentaService;
import mx.unam.aragon.elZorro.repository.MovimientoProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/caja")
@PreAuthorize("hasRole('CAJA')")
public class CorteCajaController {

    @Autowired
    private MovimientoProductoService movimientoProductoService;

    @Autowired
    private MovimientoProductoRepository movimientoProductoRepository;

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private EmpleadoService empleadoService;

    /**
     * DTO para transferir datos de movimientos de productos a la vista
     */
    public static class ProductoMovimientoDto {
        private String nombreProducto;
        private Integer stockInicial;
        private Integer vendido;
        private Integer stockFinal;

        // Constructores
        public ProductoMovimientoDto() {}

        public ProductoMovimientoDto(String nombreProducto, Integer stockInicial, Integer vendido, Integer stockFinal) {
            this.nombreProducto = nombreProducto;
            this.stockInicial = stockInicial;
            this.vendido = vendido;
            this.stockFinal = stockFinal;
        }

        // Getters y Setters
        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

        public Integer getStockInicial() { return stockInicial; }
        public void setStockInicial(Integer stockInicial) { this.stockInicial = stockInicial; }

        public Integer getVendido() { return vendido; }
        public void setVendido(Integer vendido) { this.vendido = vendido; }

        public Integer getStockFinal() { return stockFinal; }
        public void setStockFinal(Integer stockFinal) { this.stockFinal = stockFinal; }
    }

    @GetMapping("/corte")
    public String corte(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        EmpleadoEntity empleado = empleadoService.findByUsername(username);

        // Verificar si hay movimientos abiertos para este empleado
        List<MovimientoProductoEntity> movimientosAbiertos = movimientoProductoRepository.findMovimientosAbiertos(empleado);
        boolean cajaAbierta = !movimientosAbiertos.isEmpty();

        model.addAttribute("cajaAbierta", cajaAbierta);
        model.addAttribute("empleado", empleado);

        if (cajaAbierta) {
            // Obtener la fecha de apertura del primer movimiento
            LocalDate fechaApertura = movimientosAbiertos.get(0).getFecha();
            model.addAttribute("fechaApertura", fechaApertura);
            model.addAttribute("mainContent", "corte/caja-abierta");
        } else {
            model.addAttribute("mainContent", "corte/abrir-corte");
        }

        return "common/layout";
    }

    @PostMapping("/iniciar")
    public String iniciar(RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        EmpleadoEntity empleado = empleadoService.findByUsername(auth.getName());

        try {
            // Verificar si ya hay una caja abierta para este empleado
            List<MovimientoProductoEntity> movimientosAbiertos = movimientoProductoRepository.findMovimientosAbiertos(empleado);
            if (!movimientosAbiertos.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Ya tienes una caja abierta");
                return "redirect:/caja/corte";
            }

            // Obtener todos los productos
            List<ProductoEntity> productos = productoService.findAll();

            if (productos.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No hay productos registrados para abrir caja");
                return "redirect:/caja/corte";
            }

            // Crear un movimiento por cada producto
            LocalDate fechaHoy = LocalDate.now();
            for (ProductoEntity producto : productos) {
                MovimientoProductoEntity movimiento = MovimientoProductoEntity.builder()
                        .empleado(empleado)
                        .fecha(fechaHoy)
                        .cantidadInicio(producto.getStock()) // Stock actual del producto
                        .cantidadFin(null) // Se llenará al cerrar
                        .producto(producto)
                        .build();

                movimientoProductoService.save(movimiento);
            }

            redirectAttributes.addFlashAttribute("success", "Caja abierta correctamente. Se registraron " + productos.size() + " productos");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al abrir la caja: " + e.getMessage());
        }

        return "redirect:/caja/corte";
    }

    @PostMapping("/cerrar")
    public String cerrar(Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        EmpleadoEntity empleado = empleadoService.findByUsername(auth.getName());

        try {
            // Verificar si hay movimientos abiertos
            List<MovimientoProductoEntity> movimientosAbiertos = movimientoProductoRepository.findMovimientosAbiertos(empleado);

            if (movimientosAbiertos.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "No hay caja abierta para cerrar");
                return "redirect:/caja/corte";
            }

            LocalDate fechaApertura = movimientosAbiertos.get(0).getFecha();
            LocalDate fechaCierre = LocalDate.now();

            // Convertir fechas para consultar ventas
            LocalDateTime fechaInicio = fechaApertura.atStartOfDay();
            LocalDateTime fechaFin = fechaCierre.atTime(23, 59, 59);

            // Obtener ventas del período
            List<VentaEntity> ventas = ventaService.findByFechaBetween(fechaInicio, fechaFin);

            // Calcular ventas por producto
            Map<Long, Integer> ventasPorProducto = new HashMap<>();
            double totalVentas = 0;

            for (VentaEntity venta : ventas) {
                List<DetalleVentaEntity> detalles = detalleVentaService.findByVenta(venta.getId());
                for (DetalleVentaEntity detalle : detalles) {
                    totalVentas += detalle.getCantidad() * detalle.getPrecioUnitario();
                    Long productoId = detalle.getProducto().getId();
                    ventasPorProducto.merge(productoId, detalle.getCantidad(), Integer::sum);
                }
            }

            // Preparar datos para la vista
            List<ProductoMovimientoDto> productosMovimiento = new ArrayList<>();

            // Actualizar movimientos con stock final
            for (MovimientoProductoEntity movimiento : movimientosAbiertos) {
                ProductoEntity producto = movimiento.getProducto();

                // Obtener stock actual del producto
                ProductoEntity productoActual = productoService.findById(producto.getId());

                // Actualizar cantidad final
                movimiento.setCantidadFin(productoActual.getStock());
                movimientoProductoService.save(movimiento);

                // Calcular cantidades para el reporte
                Integer stockInicial = movimiento.getCantidadInicio();
                Integer stockFinal = movimiento.getCantidadFin();
                Integer vendido = ventasPorProducto.getOrDefault(producto.getId(), 0);

                // Crear DTO para la vista
                ProductoMovimientoDto dto = new ProductoMovimientoDto(
                        producto.getNombre(),
                        stockInicial,
                        vendido,
                        stockFinal
                );
                productosMovimiento.add(dto);
            }

            // Agregar datos al modelo
            model.addAttribute("fechaApertura", fechaApertura);
            model.addAttribute("fechaCierre", fechaCierre);
            model.addAttribute("totalVentas", totalVentas);
            model.addAttribute("cantidadVentas", ventas.size());
            model.addAttribute("productosMovimiento", productosMovimiento);
            model.addAttribute("mainContent", "corte/cerrar-corte");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cerrar la caja: " + e.getMessage());
            return "redirect:/caja/corte";
        }

        return "common/layout";
    }

    /**
     * Método adicional para consultar histórico de cortes
     */
    @GetMapping("/historico")
    public String historico(@RequestParam(required = false) String fecha, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        EmpleadoEntity empleado = empleadoService.findByUsername(auth.getName());

        LocalDate fechaConsulta = fecha != null ? LocalDate.parse(fecha) : LocalDate.now();

        List<MovimientoProductoEntity> movimientos = movimientoProductoRepository
                .findMovimientosCerrados(fechaConsulta, empleado);

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("fechaConsulta", fechaConsulta);
        model.addAttribute("empleado", empleado);
        model.addAttribute("mainContent", "corte/historico");

        return "common/layout";
    }
}