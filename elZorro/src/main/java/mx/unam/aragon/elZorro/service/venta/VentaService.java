package mx.unam.aragon.elZorro.service.venta;

import mx.unam.aragon.elZorro.model.dto.venta.CarritoDTO;
import mx.unam.aragon.elZorro.model.entity.DetalleVentaEntity;
import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.model.entity.VentaEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaService {
    VentaEntity save(VentaEntity venta);
    List<VentaEntity> findAll();
    void deleteById(Long id);
    VentaEntity findById(Long id);

    /**
     * Procesar una venta desde el carrito
     */
    VentaEntity procesarVenta(CarritoDTO carrito) throws Exception;

    /**
     * Validar que la venta se pueda procesar
     */
    boolean validarVenta(CarritoDTO carrito);

    /**
     * Obtener errores de validación
     */
    String obtenerErroresValidacion(CarritoDTO carrito);

    /**
     * Verificar stock antes de procesar
     */
    boolean verificarStockDisponible(CarritoDTO carrito);

    List<ProductoEntity> obtenerProductosPorVenta(Long ventaId);

    public List<DetalleVentaEntity> obtenerDetallesPorVenta(Long ventaId);

    List<VentaEntity> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
