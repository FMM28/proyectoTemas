package mx.unam.aragon.elZorro.service.cliente;

import mx.unam.aragon.elZorro.model.entity.ClienteEntity;
import mx.unam.aragon.elZorro.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional
    public ClienteEntity save(ClienteEntity cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteEntity> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public Optional<ClienteEntity> findById(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public long count() {
        return clienteRepository.count();
    }

    @Override
    public boolean existsById(Long id) {return clienteRepository.existsById(id);}

    @Override
    public Optional<ClienteEntity> findByCorreo(String correo) {
        return clienteRepository.findByCorreo(correo);
    }

    @Override
    public Optional<ClienteEntity> findByTelefono(String telefono) {
        return clienteRepository.findByTelefono(telefono);
    }
}
