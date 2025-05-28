package mx.unam.aragon.elZorro.model.dto.venta;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoCarritoDTO {
    private Long id;
    private String nombre;
    private Double precio;
    private Integer stock;
    private String imagen;
    private String categoria;

    // Constructor desde ProductoEntity
    public ProductoCarritoDTO(mx.unam.aragon.elZorro.model.entity.ProductoEntity producto) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.precio = producto.getPrecio();
        this.stock = producto.getStock();
        this.imagen = producto.getImagen();
        this.categoria = producto.getCategoria() != null ? producto.getCategoria().getNombre() : "";
    }

    // Validar si hay stock suficiente
    public boolean tieneStockSuficiente(Integer cantidadSolicitada) {
        return this.stock >= cantidadSolicitada;
    }
}