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

    // NUEVO: Método para búsqueda combinada
    @Override
    @Transactional(readOnly = true)
    public List<ProveedorEntity> buscarProveedores(String rfc, String razonSocial, String correo) {
        try {
            // Convertir strings vacíos a null para la query
            String rfcParam = (rfc != null && !rfc.trim().isEmpty()) ? rfc.trim() : null;
            String razonSocialParam = (razonSocial != null && !razonSocial.trim().isEmpty()) ? razonSocial.trim() : null;
            String correoParam = (correo != null && !correo.trim().isEmpty()) ? correo.trim() : null;

            return proveedorRepository.buscarProveedoresPorCriterios(rfcParam, razonSocialParam, correoParam);
        } catch (Exception e) {
            System.err.println("Error en búsqueda de proveedores: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Devolver lista vacía en caso de error
        }
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

        try {
            List<ProveedorEntity> resultados;
            String terminoLimpio = termino.trim();

            switch (campo.toLowerCase()) {
                case "rfc":
                    resultados = proveedorRepository.findSugerenciasRfcSinLimite(terminoLimpio);
                    break;
                case "razonsocial": // CORREGIDO: Era "razonsocial", ahora es "razonSocial"
                    resultados = proveedorRepository.findSugerenciasRazonSocialSinLimite(terminoLimpio);
                    break;
                case "correo":
                    resultados = proveedorRepository.findSugerenciasCorreoSinLimite(terminoLimpio);
                    break;
                default:
                    return List.of();
            }

            return resultados.stream()
                    .limit(Math.max(1, limite)) // Asegurar que el límite sea al menos 1
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error al obtener sugerencias para " + campo + ": " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
}