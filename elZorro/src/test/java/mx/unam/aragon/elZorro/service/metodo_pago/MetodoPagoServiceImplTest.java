package mx.unam.aragon.elZorro.service.metodo_pago;

import mx.unam.aragon.elZorro.model.entity.MetodoPagoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MetodoPagoServiceImplTest {
    @Autowired
    private MetodoPagoService metodoPagoService;

    @Test
    void crearMetodo() {
        //metodoPagoService.crearMetodo("Efectivo");
        metodoPagoService.crearMetodo("Tarjeta de credito");
        metodoPagoService.crearMetodo("Vales de despensa");
    }
}