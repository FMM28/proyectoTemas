package mx.unam.aragon.elZorro.service.producto;

import mx.unam.aragon.elZorro.model.entity.CategoriaEntity;
import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import mx.unam.aragon.elZorro.model.enums.EstatusProveedor;
import mx.unam.aragon.elZorro.model.enums.RegimenFiscal;
import mx.unam.aragon.elZorro.service.categoria.CategoriaService;
import mx.unam.aragon.elZorro.service.proveedor.ProveedorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductoServiceImplTest {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProveedorService proveedorService;

    @Test
    void crearProducto() {
        CategoriaEntity categoria1 = categoriaService.findById(1L);
        CategoriaEntity categoria2 = categoriaService.findById(2L);

        ProveedorEntity proveedor1 = ProveedorEntity.builder()
                .razonSocial("TecnoSuministros SA de CV")
                .rfc("TSUM800101ABC")
                .direccion("Av. Tecnológico 123")
                .codigoPostal("04500")
                .regimenFiscal(RegimenFiscal._601)
                .correo("ventas@tecnosuministros.com")
                .telefono("5551234567")
                .contactoNombre("Ing. Roberto Martínez")
                .estatus(EstatusProveedor.ACTIVO)
                .build();

        ProveedorEntity proveedor2 = ProveedorEntity.builder()
                .razonSocial("Distribuidora Industrial del Norte SA de CV")
                .rfc("DIN850602XYZ")
                .direccion("Calle Industria 456, Parque Industrial")
                .codigoPostal("64390")
                .regimenFiscal(RegimenFiscal._603)
                .correo("contacto@dinorte.com.mx")
                .telefono("8187654321")
                .contactoNombre("Lic. María Fernanda Ruiz")
                .estatus(EstatusProveedor.ACTIVO)
                .build();

        proveedorService.save(proveedor1);
        proveedorService.save(proveedor2);

        ProductoEntity producto1 = ProductoEntity.builder()
                .nombre("Producto1")
                .stock(5)
                .imagen("imagen")
                .categoria(categoria1)
                .proveedor(proveedor1)
                .precio(20.20)
                .build();

        ProductoEntity producto2 = ProductoEntity.builder()
                .nombre("Producto2")
                .stock(6)
                .imagen("imagen2")
                .categoria(categoria2)
                .proveedor(proveedor2)
                .precio(50.50)
                .build();

        productoService.save(producto1);
        productoService.save(producto2);

    }

}