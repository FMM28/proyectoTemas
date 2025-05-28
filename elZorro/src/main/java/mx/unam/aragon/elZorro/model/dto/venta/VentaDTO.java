package mx.unam.aragon.elZorro.model.dto.venta;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VentaDTO {
    private Long clienteId;
    private String clienteNombre; // Para mostrar en la vista
    private Long empleadoId;
    private String empleadoNombre; // Para mostrar en la vista
    private Long metodoPagoId;
    private String metodoPagoNombre; // Para mostrar en la vista
    private LocalDateTime fecha;

    // Constructor básico con fecha actual
    public VentaDTO(Long empleadoId) {
        this.empleadoId = empleadoId;
        this.fecha = LocalDateTime.now();
    }

    // Validar que los datos mínimos estén presentes
    public boolean esValida() {
        return empleadoId != null && metodoPagoId != null;
    }

    // Validar que tenga cliente (para ventas que requieren cliente)
    public boolean tieneCliente() {
        return clienteId != null;
    }

    // Establecer cliente con nombre
    public void establecerCliente(Long clienteId, String clienteNombre) {
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
    }

    // Establecer empleado con nombre
    public void establecerEmpleado(Long empleadoId, String empleadoNombre) {
        this.empleadoId = empleadoId;
        this.empleadoNombre = empleadoNombre;
    }

    // Establecer método de pago con nombre
    public void establecerMetodoPago(Long metodoPagoId, String metodoPagoNombre) {
        this.metodoPagoId = metodoPagoId;
        this.metodoPagoNombre = metodoPagoNombre;
    }
}
