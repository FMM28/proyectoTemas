package mx.unam.aragon.elZorro.service.recibo;

import mx.unam.aragon.elZorro.model.entity.DetalleVentaEntity;
import mx.unam.aragon.elZorro.model.entity.VentaEntity;
import mx.unam.aragon.elZorro.model.json.Producto;
import mx.unam.aragon.elZorro.model.json.Recibo;
import mx.unam.aragon.elZorro.service.detalle_venta.DetalleVentaService;
import mx.unam.aragon.elZorro.service.venta.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReciboService {
    @Autowired
    private VentaService ventaService;
    @Autowired
    private DetalleVentaService detalleVentaService;

    public Recibo getRecibo(Long id) {
        VentaEntity venta = ventaService.findById(id);
        List<DetalleVentaEntity> detalles = detalleVentaService.findByVenta(venta.getId());
        List<Producto> productos = new ArrayList<>();

        double total = 0.0;

        for (DetalleVentaEntity detalle : detalles) {
            Producto producto = new Producto(detalle.getProducto().getNombre(),detalle.getCantidad(),detalle.getPrecioUnitario());
            productos.add(producto);
            total += detalle.getPrecioUnitario()*detalle.getCantidad();
        }

        String vendedor = venta.getEmpleado().getNombre()+" "+venta.getEmpleado().getApellidoPaterno()+" "+venta.getEmpleado().getApellidoMaterno();
        String cliente = venta.getCliente().getNombre()+" "+venta.getCliente().getApellidoPaterno()+" "+venta.getCliente().getApellidoMaterno();

        return new Recibo(venta.getId(),vendedor,venta.getFecha(),cliente,productos,total);
    }
}
