package mx.unam.aragon.elZorro.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity(name = "movimiento_producto")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovimientoProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private ProductoEntity producto;

    @Column(name = "cantidad_inicio")
    private Integer cantidadInicio;

    @Column(name = "cantidad_fin")
    private Integer cantidadFin;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private EmpleadoEntity empleado;

    @Column(name = "fecha")
    private LocalDate fecha;
}
