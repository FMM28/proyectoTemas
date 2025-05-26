package mx.unam.aragon.elZorro.service.proveedor;

import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import mx.unam.aragon.elZorro.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // Métodos existentes (se mantienen igual)
    @Override
    public ProveedorEntity save(ProveedorEntity proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEntity> findAll() {
        return proveedorRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        proveedorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorEntity findById(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorEntity findByRfc(String rfc) {
        return proveedorRepository.findByRfc(rfc).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorEntity findByCorreo(String correo) {
        return proveedorRepository.findByCorreo(correo).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorEntity findByRazonSocial(String razonSocial) {
        return proveedorRepository.findByRazonSocialContainingIgnoreCase(razonSocial).orElse(null);
    }

    // Métodos para sugerencias (con validación mejorada)
    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEntity> findSugerenciasRfc(String termino, int limite) {
        return getSugerencias(termino, limite, "rfc");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEntity> findSugerenciasRazonSocial(String termino, int limite) {
        return getSugerencias(termino, limite, "razonSocial");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEntity> findSugerenciasCorreo(String termino, int limite) {
        return getSugerencias(termino, limite, "correo");
    }

    private List<ProveedorEntity> getSugerencias(String termino, int limite, String campo) {
        if (termino == null || termino.trim().length() < 2) {
            return List.of();
        }

        List<ProveedorEntity> resultados;
        switch (campo.toLowerCase()) {
            case "rfc":
                resultados = proveedorRepository.findSugerenciasRfc(termino.toLowerCase());
                break;
            case "razonsocial":
                resultados = proveedorRepository.findSugerenciasRazonSocial(termino.toLowerCase());
                break;
            case "correo":
                resultados = proveedorRepository.findSugerenciasCorreo(termino.toLowerCase());
                break;
            default:
                return List.of();
        }

        return resultados.stream()
                .limit(limite)
                .collect(Collectors.toList());
    }
}