package mx.unam.aragon.elZorro.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity(name = "empleado")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpleadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empleado_id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @Column(name = "apellido_paterno")
    private String apellidoPaterno;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private RolEntity rol;

    @Column(name = "usuario", unique = true)
    private String usuario;

    @Column(name = "password")
    private String password;
}
