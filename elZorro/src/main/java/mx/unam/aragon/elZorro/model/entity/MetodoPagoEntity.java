package mx.unam.aragon.elZorro.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "metodo_pago")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetodoPagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metodo_id")
    private Long id;

    @Column(name="nombre")
    private String nombre;
}
