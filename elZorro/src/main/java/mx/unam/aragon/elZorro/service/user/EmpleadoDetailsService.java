package mx.unam.aragon.elZorro.service.user;

import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import mx.unam.aragon.elZorro.repository.EmpleadoRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoDetailsService implements UserDetailsService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoDetailsService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmpleadoEntity empleado = empleadoRepository.findByUsuario(username);

        if (empleado == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        return User.withUsername(empleado.getUsuario())
                .password(empleado.getPassword())
                .roles("ADMIN")
                .authorities("ROLE_ADMIN")
                .build();
    }
}

