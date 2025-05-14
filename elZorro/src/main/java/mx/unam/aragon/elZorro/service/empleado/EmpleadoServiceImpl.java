package mx.unam.aragon.elZorro.service.empleado;

import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import mx.unam.aragon.elZorro.model.entity.RolEntity;
import mx.unam.aragon.elZorro.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public EmpleadoEntity save(EmpleadoEntity empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoEntity> findAll() {
        return empleadoRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        empleadoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoEntity findById(Long id) {
        Optional<EmpleadoEntity> empleado = empleadoRepository.findById(id);
        return empleado.orElse(null);
    }

    @Override
    @Transactional
    public EmpleadoEntity crearEmpleado(String usuario, String passwordPlano, RolEntity rol) {
        EmpleadoEntity empleado = EmpleadoEntity.builder()
                .usuario(usuario)
                .password(passwordEncoder.encode(passwordPlano))
                .rol(rol)
                .build();

        return empleadoRepository.save(empleado);
    }
}
