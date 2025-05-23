package mx.unam.aragon.elZorro.model.entity;

import jakarta.persistence.*;
import lombok.*;
import mx.unam.aragon.elZorro.model.enums.EstatusProveedor;
import mx.unam.aragon.elZorro.model.enums.RegimenFiscal;

@Data
@Entity(name = "proveedor")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProveedorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proveedor_id")
    private Long id;

    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "rfc")
    private String rfc;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "c_postal")
    private String codigoPostal;

    @Enumerated(EnumType.STRING)
    @Column(name = "regimen_fiscal")
    private RegimenFiscal regimenFiscal;

    @Column(name = "correo")
    private String correo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "contacto_nombre")
    private String contactoNombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus")
    private EstatusProveedor estatus;
}