package mx.unam.aragon.elZorro.service.empleado;

import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import mx.unam.aragon.elZorro.model.entity.RolEntity;
import mx.unam.aragon.elZorro.service.rol.RolServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmpleadoServiceImplTest {
    @Autowired
    EmpleadoServiceImpl empleadoService;
    @Autowired
    RolServiceImpl rolService;
    @Test
    void crearEmpleado() {


        RolEntity rol = rolService.findById(1L);

        empleadoService.crearEmpleado("admin1","admin1","admin1","admin11","1234",rol);
    }
}