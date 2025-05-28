package mx.unam.aragon.elZorro.service.venta;

import mx.unam.aragon.elZorro.model.dto.venta.CarritoDTO;
import mx.unam.aragon.elZorro.model.dto.venta.DetalleVentaDTO;
import mx.unam.aragon.elZorro.model.entity.*;
import mx.unam.aragon.elZorro.repository.DetalleVentaRepository;
import mx.unam.aragon.elZorro.repository.ProductoRepository;
import mx.unam.aragon.elZorro.repository.VentaRepository;
import mx.unam.aragon.elZorro.service.cliente.ClienteService;
import mx.unam.aragon.elZorro.service.empleado.EmpleadoService;
import mx.unam.aragon.elZorro.service.metodo_pago.MetodoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private MetodoPagoService metodoPagoService;

    @Override
    @Transactional
    public VentaEntity save(VentaEntity venta) {
        return ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaEntity> findAll() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public VentaEntity findById(Long id) {
        Optional<VentaEntity> venta = ventaRepository.findById(id);
        return venta.orElse(null);
    }



    @Override
    @Transactional
    public VentaEntity procesarVenta(CarritoDTO carrito) throws Exception {


        // Validaciones previas
        if (!validarVenta(carrito)) {
            throw new IllegalArgumentException("La venta no es válida: " + obtenerErroresValidacion(carrito));
        }

        // Verificar stock una vez más antes de procesar
        if (!verificarStockDisponible(carrito)) {
            throw new IllegalStateException("No hay stock suficiente para algunos productos");
        }

        try {
            // 1. Crear la entidad VentaEntity
            VentaEntity venta = crearVentaEntity(carrito);
            venta = ventaRepository.save(venta);

            // 2. Crear los detalles y actualizar stock
            List<DetalleVentaEntity> detalles = new ArrayList<>();

            for (DetalleVentaDTO detalleDTO : carrito.getDetalles()) {
                // Crear detalle
                DetalleVentaEntity detalle = crearDetalleVentaEntity(detalleDTO, venta);
                detalles.add(detalle);

                // Actualizar stock del producto
                ProductoEntity producto = productoRepository.findById(detalleDTO.getProducto().getId())
                        .orElseThrow(() -> new IllegalStateException("Producto no encontrado durante procesamiento"));

                producto.setStock(producto.getStock() - detalleDTO.getCantidad());
                productoRepository.save(producto);
            }

            // 3. Guardar todos los detalles
            detalleVentaRepository.saveAll(detalles);

            return venta;

        } catch (Exception e) {
            System.err.println("Error al procesar venta: " + e.getMessage());
            throw new Exception("Error al procesar la venta: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean validarVenta(CarritoDTO carrito) {
        return carrito != null
                && carrito.esValidoParaProcesar()
                && verificarStockDisponible(carrito);
    }

    @Override
    public String obtenerErroresValidacion(CarritoDTO carrito) {
        List<String> errores = new ArrayList<>();

        if (carrito == null) {
            errores.add("El carrito está vacío");
            return String.join(", ", errores);
        }

        if (carrito.estaVacio()) {
            errores.add("No hay productos en el carrito");
        }

        if (carrito.getVentaInfo().getEmpleadoId() == null) {
            errores.add("No se ha establecido el empleado");
        }

        if (carrito.getVentaInfo().getMetodoPagoId() == null) {
            errores.add("No se ha establecido el método de pago");
        }

        if (!verificarStockDisponible(carrito)) {
            errores.add("Stock insuficiente para algunos productos");
        }

        // Validar cada detalle
        for (DetalleVentaDTO detalle : carrito.getDetalles()) {
            if (!detalle.esValido()) {
                errores.add("Producto inválido: " + detalle.getProducto().getNombre());
            }
        }

        return errores.isEmpty() ? "" : String.join(", ", errores);
    }

    @Override
    public boolean verificarStockDisponible(CarritoDTO carrito) {
        for (DetalleVentaDTO detalle : carrito.getDetalles()) {
            // Verificar stock actual en base de datos
            ProductoEntity producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElse(null);

            if (producto == null || producto.getStock() < detalle.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    // Métodos auxiliares privados

    private VentaEntity crearVentaEntity(CarritoDTO carrito) throws Exception {
        VentaEntity venta = new VentaEntity();

        // Establecer fecha
        venta.setFecha(LocalDateTime.now());

        // Establecer empleado
        EmpleadoEntity empleado = empleadoService.findById(carrito.getVentaInfo().getEmpleadoId());
        if (empleado == null) {
            throw new IllegalArgumentException("Empleado no encontrado");
        }
        venta.setEmpleado(empleado);

        // Establecer método de pago
        MetodoPagoEntity metodoPago = metodoPagoService.findById(carrito.getVentaInfo().getMetodoPagoId());
        if (metodoPago == null) {
            throw new IllegalArgumentException("Método de pago no encontrado");
        }
        venta.setMetodoPago(metodoPago);

        // Establecer cliente (opcional)
        if (carrito.getVentaInfo().getClienteId() != null) {
            ClienteEntity cliente = clienteService.findById(carrito.getVentaInfo().getClienteId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
            venta.setCliente(cliente);
        }

        return venta;
    }

    private DetalleVentaEntity crearDetalleVentaEntity(DetalleVentaDTO detalleDTO, VentaEntity venta) throws Exception {
        DetalleVentaEntity detalle = new DetalleVentaEntity();

        // Establecer venta
        detalle.setVenta(venta);

        // Establecer producto
        ProductoEntity producto = productoRepository.findById(detalleDTO.getProducto().getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        detalle.setProducto(producto);

        // Establecer cantidad y precio
        detalle.setCantidad(detalleDTO.getCantidad());
        detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());

        return detalle;
    }

    @Override
    public List<ProductoEntity> obtenerProductosPorVenta(Long ventaId) {
        return ventaRepository.obtenerProductosPorVentaId(ventaId);
    }

    @Override
    public List<DetalleVentaEntity> obtenerDetallesPorVenta(Long ventaId) {
        return detalleVentaRepository.findByVenta(ventaId);
    }
}
