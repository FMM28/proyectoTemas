package mx.unam.aragon.elZorro.controller.administracion.proveedor;

import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import mx.unam.aragon.elZorro.service.proveedor.ProveedorService;
import mx.unam.aragon.elZorro.validator.ProveedorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador especializado en la gestión (edición y eliminación) de proveedores
 * Responsabilidad: Manejo de operaciones CRUD de edición y eliminación
 */
@Controller
@RequestMapping("/administracion/proveedor")
@PreAuthorize("hasRole('ADMIN')")
public class ProveedorGestionController {

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private ProveedorValidator proveedorValidator;

    private static final String REDIRECT_BUSCAR = "redirect:/administracion/proveedor/buscar";

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        ProveedorEntity proveedor = proveedorService.findById(id);

        if (proveedor == null) {
            redirectAttributes.addFlashAttribute("error",
                    String.format("Proveedor con ID %d no encontrado", id));
            return REDIRECT_BUSCAR;
        }

        model.addAttribute("proveedor", proveedor);
        model.addAttribute("mainContent", "proveedor/editar_proveedor");
        return "common/layout";
    }

    @PostMapping("/editar/{id}")
    public String actualizarProveedor(
            @PathVariable Long id,
            @ModelAttribute ProveedorEntity proveedorActualizado,
            RedirectAttributes redirectAttributes) {

        try {
            ProveedorEntity proveedorExistente = proveedorService.findById(id);

            if (proveedorExistente == null) {
                redirectAttributes.addFlashAttribute("error", "Proveedor no encontrado");
                return REDIRECT_BUSCAR;
            }

            actualizarCamposProveedor(proveedorExistente, proveedorActualizado);
            proveedorService.save(proveedorExistente);

            redirectAttributes.addFlashAttribute("exito", "Proveedor actualizado exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al actualizar: " + e.getMessage());
        }

        return REDIRECT_BUSCAR;
    }

    @PostMapping("/eliminar")
    public String eliminarProveedor(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes) {

        try {
            proveedorService.deleteById(id);
            redirectAttributes.addFlashAttribute("exito",
                    "Proveedor eliminado exitosamente del sistema");
        } catch (EmptyResultDataAccessException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error: El proveedor con ID " + id + " no existe");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al eliminar proveedor: " + e.getMessage());
        }

        return REDIRECT_BUSCAR;
    }

    /**
     * Actualiza todos los campos del proveedor existente con los datos del proveedor actualizado
     */
    private void actualizarCamposProveedor(ProveedorEntity existente, ProveedorEntity actualizado) {
        existente.setRazonSocial(actualizado.getRazonSocial());
        existente.setRfc(actualizado.getRfc());
        existente.setDireccion(actualizado.getDireccion());
        existente.setCodigoPostal(actualizado.getCodigoPostal());
        existente.setRegimenFiscal(actualizado.getRegimenFiscal());
        existente.setCorreo(actualizado.getCorreo());
        existente.setTelefono(actualizado.getTelefono());
        existente.setContactoNombre(actualizado.getContactoNombre());
        existente.setEstatus(actualizado.getEstatus());
    }

    @InitBinder("proveedor")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(proveedorValidator);
    }
}