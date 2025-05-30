package mx.unam.aragon.elZorro.repository;

import mx.unam.aragon.elZorro.model.entity.MovimientoProductoEntity;
import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovimientoProductoRepository extends JpaRepository<MovimientoProductoEntity, Long> {

    /**
     * Encuentra movimientos sin cerrar (cantidadFin es null) para un empleado específico
     */
    @Query("SELECT m FROM movimiento_producto m WHERE m.empleado = :empleado AND m.cantidadFin IS NULL")
    List<MovimientoProductoEntity> findMovimientosAbiertos(@Param("empleado") EmpleadoEntity empleado);

    /**
     * Encuentra movimientos por fecha y empleado
     */
    @Query("SELECT m FROM movimiento_producto m WHERE m.fecha = :fecha AND m.empleado = :empleado ORDER BY m.producto.nombre")
    List<MovimientoProductoEntity> findByFechaAndEmpleado(@Param("fecha") LocalDate fecha, @Param("empleado") EmpleadoEntity empleado);

    /**
     * Verifica si existe una caja abierta para cualquier empleado
     */
    @Query("SELECT COUNT(m) > 0 FROM movimiento_producto m WHERE m.cantidadFin IS NULL")
    boolean existeCajaAbierta();

    /**
     * Encuentra movimientos por fecha específica
     */
    List<MovimientoProductoEntity> findByFecha(LocalDate fecha);

    /**
     * Encuentra movimientos cerrados por fecha y empleado
     */
    @Query("SELECT m FROM movimiento_producto m WHERE m.fecha = :fecha AND m.empleado = :empleado AND m.cantidadFin IS NOT NULL ORDER BY m.producto.nombre")
    List<MovimientoProductoEntity> findMovimientosCerrados(@Param("fecha") LocalDate fecha, @Param("empleado") EmpleadoEntity empleado);
}