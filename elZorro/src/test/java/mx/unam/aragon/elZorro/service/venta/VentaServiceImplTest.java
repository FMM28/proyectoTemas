package mx.unam.aragon.elZorro.service.venta;

import mx.unam.aragon.elZorro.model.entity.*;
import mx.unam.aragon.elZorro.model.json.Producto;
import mx.unam.aragon.elZorro.service.cliente.ClienteServiceImpl;
import mx.unam.aragon.elZorro.service.detalle_venta.DetalleVentaServiceImpl;
import mx.unam.aragon.elZorro.service.empleado.EmpleadoService;
import mx.unam.aragon.elZorro.service.empleado.EmpleadoServiceImpl;
import mx.unam.aragon.elZorro.service.metodo_pago.MetodoPagoService;
import mx.unam.aragon.elZorro.service.metodo_pago.MetodoPagoServiceImpl;
import mx.unam.aragon.elZorro.service.producto.ProductoServiceImpl;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VentaServiceImplTest {
    @Autowired
    private VentaServiceImpl ventaService;
    @Autowired
    private ProductoServiceImpl productoService;
    @Autowired
    private ClienteServiceImpl clienteService;
    @Autowired
    private DetalleVentaServiceImpl detalleVentaService;
    @Autowired
    private EmpleadoServiceImpl empleadoService;
    @Autowired
    private MetodoPagoServiceImpl metodoPagoService;

    @Test
    void crearVenta() {
        ProductoEntity producto1 = productoService.findById(1L);
        ProductoEntity producto2 = productoService.findById(2L);

        ClienteEntity cliente = ClienteEntity.builder()
                .nombre("Francisco")
                .apellidoPaterno("Marquez")
                .apellidoMaterno("Maya")
                .correo("marquez.maya.francisco.28@gmail.com")
                .telefono("5586193550")
                .build();

        clienteService.save(cliente);

        EmpleadoEntity empleado = empleadoService.findById(3L);

        MetodoPagoEntity metodoPago = MetodoPagoEntity.builder()
                .nombre("Efectivo")
                .build();

        metodoPagoService.save(metodoPago);

        VentaEntity venta = VentaEntity.builder()
                .cliente(cliente)
                .empleado(empleado)
                .fecha(LocalDateTime.now())
                .metodoPago(metodoPago)
                .build();

        ventaService.save(venta);

        DetalleVentaEntity detalleVenta1 = DetalleVentaEntity.builder()
                .venta(venta)
                .cantidad(5)
                .producto(producto1)
                .precioUnitario(producto1.getPrecio())
                .build();

        DetalleVentaEntity detalleVenta2 = DetalleVentaEntity.builder()
                .venta(venta)
                .cantidad(10)
                .producto(producto2)
                .precioUnitario(producto2.getPrecio())
                .build();

        detalleVentaService.save(detalleVenta1);
        detalleVentaService.save(detalleVenta2);
    }
}