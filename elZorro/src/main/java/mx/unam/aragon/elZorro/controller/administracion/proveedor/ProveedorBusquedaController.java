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

            ProveedorEntity proveedor = ejecutarBusqueda(rfc, razonSocial, correo);

            if (proveedor != null) {
                model.addAttribute("proveedorEncontrado", proveedor);
            } else {
                model.addAttribute("error", "No se encontró ningún proveedor con los criterios proporcionados");
            }

        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al buscar el proveedor: " + e.getMessage());
        }

        return mostrarFormularioBusqueda(model);
    }

    @GetMapping("/sugerencias/rfc")
    @ResponseBody
    public ResponseEntity<List<ProveedorEntity>> obtenerSugerenciasRfc(
            @RequestParam String termino,
            @RequestParam(defaultValue = "5") int limite) {

        List<ProveedorEntity> sugerencias = proveedorService.findSugerenciasRfc(termino, limite);
        return ResponseEntity.ok(sugerencias);
    }

    @GetMapping("/sugerencias/razon-social")
    @ResponseBody
    public ResponseEntity<List<ProveedorEntity>> obtenerSugerenciasRazonSocial(
            @RequestParam String termino,
            @RequestParam(defaultValue = "5") int limite) {

        List<ProveedorEntity> sugerencias = proveedorService.findSugerenciasRazonSocial(termino, limite);
        return ResponseEntity.ok(sugerencias);
    }

    @GetMapping("/sugerencias/correo")
    @ResponseBody
    public ResponseEntity<List<ProveedorEntity>> obtenerSugerenciasCorreo(
            @RequestParam String termino,
            @RequestParam(defaultValue = "5") int limite) {

        List<ProveedorEntity> sugerencias = proveedorService.findSugerenciasCorreo(termino, limite);
        return ResponseEntity.ok(sugerencias);
    }


    private boolean validarCriteriosBusqueda(String rfc, String razonSocial, String correo) {
        return !((rfc == null || rfc.isEmpty()) &&
                (razonSocial == null || razonSocial.isEmpty()) &&
                (correo == null || correo.isEmpty()));
    }

    private ProveedorEntity ejecutarBusqueda(String rfc, String razonSocial, String correo) {
        if (rfc != null && !rfc.isEmpty()) {
            return proveedorService.findByRfc(rfc);
        } else if (razonSocial != null && !razonSocial.isEmpty()) {
            return proveedorService.findByRazonSocial(razonSocial);
        } else if (correo != null && !correo.isEmpty()) {
            return proveedorService.findByCorreo(correo);
        }
        return null;
    }
}