package mx.unam.aragon.elZorro.controller.administracion.proveedor;

import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import mx.unam.aragon.elZorro.service.proveedor.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador especializado en la búsqueda de proveedores
 * Responsabilidad: Manejo de búsquedas y sugerencias de proveedores
 */
@Controller
@RequestMapping("/administracion/proveedor")
@PreAuthorize("hasRole('ADMIN')")
public class ProveedorBusquedaController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("/buscar")
    public String mostrarFormularioBusqueda(Model model) {
        model.addAttribute("mainContent", "proveedor/buscar_proveedor");
        return "common/layout";
    }

    @PostMapping("/buscar")
    public String buscarProveedor(
            @RequestParam(required = false) String rfc,
            @RequestParam(required = false) String razonSocial,
            @RequestParam(required = false) String correo,
            Model model) {

        try {
            if (!validarCriteriosBusqueda(rfc, razonSocial, correo)) {
                model.addAttribute("error", "Debe proporcionar al menos un criterio de búsqueda");
                return mostrarFormularioBusqueda(model);
            }

            // CAMBIO: Ahora busca con múltiples criterios y devuelve una lista
            List<ProveedorEntity> proveedores = ejecutarBusqueda(rfc, razonSocial, correo);

            if (proveedores != null && !proveedores.isEmpty()) {
                if (proveedores.size() == 1) {
                    model.addAttribute("proveedorEncontrado", proveedores.get(0));
                } else {
                    model.addAttribute("proveedoresEncontrados", proveedores);
                    model.addAttribute("mensaje", "Se encontraron " + proveedores.size() + " proveedores");
                }
            } else {
                model.addAttribute("error", "No se encontró ningún proveedor con los criterios proporcionados");
            }

        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al buscar el proveedor: " + e.getMessage());
            e.printStackTrace(); // Para debugging
        }

        return mostrarFormularioBusqueda(model);
    }

    @GetMapping("/sugerencias/rfc")
    @ResponseBody
    public ResponseEntity<List<ProveedorEntity>> obtenerSugerenciasRfc(
            @RequestParam String termino,
            @RequestParam(defaultValue = "10") int limite) {

        try {
            List<ProveedorEntity> sugerencias = proveedorService.findSugerenciasRfc(termino, limite);
            return ResponseEntity.ok(sugerencias);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/sugerencias/razon-social")
    @ResponseBody
    public ResponseEntity<List<ProveedorEntity>> obtenerSugerenciasRazonSocial(
            @RequestParam String termino,
            @RequestParam(defaultValue = "10") int limite) {

        try {
            List<ProveedorEntity> sugerencias = proveedorService.findSugerenciasRazonSocial(termino, limite);
            return ResponseEntity.ok(sugerencias);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/sugerencias/correo")
    @ResponseBody
    public ResponseEntity<List<ProveedorEntity>> obtenerSugerenciasCorreo(
            @RequestParam String termino,
            @RequestParam(defaultValue = "10") int limite) {

        try {
            List<ProveedorEntity> sugerencias = proveedorService.findSugerenciasCorreo(termino, limite);
            return ResponseEntity.ok(sugerencias);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean validarCriteriosBusqueda(String rfc, String razonSocial, String correo) {
        return !((rfc == null || rfc.trim().isEmpty()) &&
                (razonSocial == null || razonSocial.trim().isEmpty()) &&
                (correo == null || correo.trim().isEmpty()));
    }

    // CAMBIO: Ahora devuelve una lista y puede buscar por múltiples criterios
    private List<ProveedorEntity> ejecutarBusqueda(String rfc, String razonSocial, String correo) {
        // Limpiar parámetros
        rfc = (rfc != null) ? rfc.trim() : null;
        razonSocial = (razonSocial != null) ? razonSocial.trim() : null;
        correo = (correo != null) ? correo.trim() : null;

        // Usar el nuevo método de búsqueda combinada
        return proveedorService.buscarProveedores(rfc, razonSocial, correo);
    }
}