package mx.unam.aragon.elZorro.model.dto.venta;

import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetalleVentaDTO {
    private ProductoCarritoDTO producto;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;

    // Constructor con cálculo automático de subtotal
    public DetalleVentaDTO(ProductoCarritoDTO producto, Integer cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
        this.subtotal = calcularSubtotal();
    }

    // Calcular subtotal
    public Double calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            BigDecimal resultado = BigDecimal.valueOf(cantidad)
                    .multiply(BigDecimal.valueOf(precioUnitario))
                    .setScale(2, RoundingMode.HALF_UP);
            this.subtotal = resultado.doubleValue();
            return this.subtotal;
        }
        return 0.0;
    }

    // Actualizar cantidad y recalcular subtotal
    public void actualizarCantidad(Integer nuevaCantidad) {
        this.cantidad = nuevaCantidad;
        this.subtotal = calcularSubtotal();
    }

    // Actualizar precio unitario y recalcular subtotal
    public void actualizarPrecioUnitario(Double nuevoPrecio) {
        this.precioUnitario = nuevoPrecio;
        this.subtotal = calcularSubtotal();
    }

    // Validar si la cantidad solicitada está disponible en stock
    public boolean validarStock() {
        return producto.tieneStockSuficiente(cantidad);
    }

    // Validar que los datos sean correctos
    public boolean esValido() {
        return producto != null
                && cantidad != null
                && cantidad > 0
                && precioUnitario != null
                && precioUnitario > 0
                && validarStock();
    }
}
