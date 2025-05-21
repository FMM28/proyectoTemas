package mx.unam.aragon.elZorro.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "cliente")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private long id;

    @Column(name="nombre")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column(name="apellido_paterno")
    @NotBlank(message = "El apellido paterno es obligatorio")
    private String apellidoPaterno;

    @Column(name="apellido_materno")
    private String apellidoMaterno;

    @Column(name="telefono")
    @NotBlank(message = "Campo obligatorio")
    private String telefono;

    @Column(name="correo")
    @NotBlank(message = "Campo obligatorio")
    private String correo;
}
