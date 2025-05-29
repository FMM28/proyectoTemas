package mx.unam.aragon.elZorro.model.dto.venta;

import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarritoDTO {
    private VentaDTO ventaInfo;
    private List<DetalleVentaDTO> detalles;
    private Double total;
    private Integer cantidadTotalItems;

    // Constructor básico
    public CarritoDTO(Long empleadoId) {
        this.ventaInfo = new VentaDTO(empleadoId);
        this.detalles = new ArrayList<>(); // Inicializar la lista
        this.total = 0.0;
        this.cantidadTotalItems = 0;
    }

    // Agregar producto al carrito
    public boolean agregarProducto(ProductoCarritoDTO producto, Integer cantidad) {

        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        // Validar stock
        if (!producto.tieneStockSuficiente(cantidad)) {
            return false;
        }

        // Buscar si el producto ya existe en el carrito
        Optional<DetalleVentaDTO> detalleExistente = detalles.stream()
                .filter(d -> d.getProducto().getId().equals(producto.getId()))
                .findFirst();

        if (detalleExistente.isPresent()) {
            // Si existe, actualizar cantidad
            DetalleVentaDTO detalle = detalleExistente.get();
            Integer nuevaCantidad = detalle.getCantidad() + cantidad;

            // Validar que no exceda el stock
            if (!producto.tieneStockSuficiente(nuevaCantidad)) {
                return false;
            }

            detalle.actualizarCantidad(nuevaCantidad);
        } else {
            // Si no existe, crear nuevo detalle
            DetalleVentaDTO nuevoDetalle = new DetalleVentaDTO(producto, cantidad);
            detalles.add(nuevoDetalle);
        }

        recalcularTotales();
        return true;
    }

    // Eliminar producto del carrito
    public boolean eliminarProducto(Long productoId) {
        boolean eliminado = detalles.removeIf(d -> d.getProducto().getId().equals(productoId));
        if (eliminado) {
            recalcularTotales();
        }
        return eliminado;
    }

    // Modificar cantidad de un producto
    public boolean modificarCantidad(Long productoId, Integer nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            return eliminarProducto(productoId);
        }

        Optional<DetalleVentaDTO> detalle = detalles.stream()
                .filter(d -> d.getProducto().getId().equals(productoId))
                .findFirst();

        if (detalle.isPresent()) {
            DetalleVentaDTO detalleVenta = detalle.get();

            // Validar stock
            if (!detalleVenta.getProducto().tieneStockSuficiente(nuevaCantidad)) {
                return false;
            }

            detalleVenta.actualizarCantidad(nuevaCantidad);
            recalcularTotales();
            return true;
        }

        return false;
    }

    // Recalcular totales del carrito
    public void recalcularTotales() {
        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        BigDecimal totalCalculado = BigDecimal.ZERO;
        Integer cantidadTotal = 0;

        for (DetalleVentaDTO detalle : detalles) {
            detalle.calcularSubtotal();
            totalCalculado = totalCalculado.add(BigDecimal.valueOf(detalle.getSubtotal()));
            cantidadTotal += detalle.getCantidad();
        }

        this.total = totalCalculado.setScale(2, RoundingMode.HALF_UP).doubleValue();
        this.cantidadTotalItems = cantidadTotal;
    }

    // Limpiar carrito
    public void limpiar() {
        this.detalles.clear();
        this.total = 0.0;
        this.cantidadTotalItems = 0;
        // No limpiar ventaInfo para mantener empleado
    }

    // Verificar si el carrito está vacío
    public boolean estaVacio() {
        return detalles == null || detalles.isEmpty();
    }

    // Obtener cantidad de un producto específico
    public Integer getCantidadProducto(Long productoId) {
        return detalles.stream()
                .filter(d -> d.getProducto().getId().equals(productoId))
                .findFirst()
                .map(DetalleVentaDTO::getCantidad)
                .orElse(0);
    }

    // Validar que el carrito esté listo para procesar
    public boolean esValidoParaProcesar() {
        return !estaVacio()
                && ventaInfo.esValida()
                && detalles.stream().allMatch(DetalleVentaDTO::esValido);
    }

    // Obtener resumen del carrito
    public String getResumen() {
        return String.format("Items: %d, Total: $%.2f", cantidadTotalItems, total);
    }
}