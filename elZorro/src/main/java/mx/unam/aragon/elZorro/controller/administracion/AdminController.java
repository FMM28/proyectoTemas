package mx.unam.aragon.elZorro.controller.administracion;

import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import mx.unam.aragon.elZorro.model.enums.EstatusProveedor;
import mx.unam.aragon.elZorro.model.enums.RegimenFiscal;
import mx.unam.aragon.elZorro.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.util.Optional;

@Controller
@RequestMapping("/administracion")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping("")
    public String administracion(Model model) {
        model.addAttribute("mainContent","admin/home");
        return "common/layout";
    }

    @GetMapping("/alta-proveedor")
    public String altaProveedor(Model model) {
        model.addAttribute("proveedor", new ProveedorEntity());
        model.addAttribute("regimenes", RegimenFiscal.values());
        model.addAttribute("mainContent","proveedor/alta_proveedor");
        return "common/layout";
    }

    @PostMapping("/alta-proveedor")
    public String guardarProveedor(@Valid @ModelAttribute ProveedorEntity proveedor,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        // Si hay errores de validación, regresamos al formulario
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("proveedor", proveedor); // Agregar el objeto proveedor al modelo
            model.addAttribute("regimenes", RegimenFiscal.values());
            model.addAttribute("mainContent", "proveedor/alta_proveedor");
            return "common/layout";
        }

        try {
            // Establecer el estatus por defecto como ACTIVO
            proveedor.setEstatus(EstatusProveedor.ACTIVO);

            // Guardar el proveedor
            proveedorRepository.save(proveedor);

            // Mensaje de éxito y redirección
            redirectAttributes.addAttribute("exito", true);
            return "redirect:/administracion/alta-proveedor";

        } catch (Exception e) {
            // En caso de error, mostrar mensaje y mantener los datos del formulario
            model.addAttribute("proveedor", proveedor); // Agregar el objeto proveedor al modelo
            model.addAttribute("error", "Error al guardar el proveedor: " + e.getMessage());
            model.addAttribute("regimenes", RegimenFiscal.values());
            model.addAttribute("mainContent", "proveedor/alta_proveedor");
            return "common/layout";
        }
    }

    @GetMapping("/buscar-proveedor")
    public String mostrarBuscarProveedor(Model model) {
        model.addAttribute("mainContent","proveedor/buscar_proveedor");
        return "common/layout"; // Asegúrate de que tu layout tenga el th:fragment="content"
    }

    @PostMapping("/buscar-proveedor")
    public String buscarProveedor(
            @RequestParam(required = false) String rfc,
            @RequestParam(required = false) String razonSocial,
            @RequestParam(required = false) String correo,
            Model model) {

        try {
            Optional<ProveedorEntity> proveedor = Optional.empty();

            if (rfc != null && !rfc.isEmpty()) {
                proveedor = proveedorRepository.findByRfc(rfc);
            } else if (razonSocial != null && !razonSocial.isEmpty()) {
                proveedor = proveedorRepository.findByRazonSocialContainingIgnoreCase(razonSocial);
            } else if (correo != null && !correo.isEmpty()) {
                proveedor = proveedorRepository.findByCorreo(correo);
            }

            if (proveedor.isPresent()) {
                System.out.println(proveedor);
                model.addAttribute("proveedorEncontrado", proveedor.get());
            } else {
                model.addAttribute("error", "No se encontró ningún proveedor con los criterios proporcionados");
            }

        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al buscar el proveedor: " + e.getMessage());
        }

        model.addAttribute("mainContent", "proveedor/buscar_proveedor");
        return "common/layout";
    }

    @PostMapping("/eliminar-proveedor")
    public String eliminarProveedor(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes) {

        try {
            proveedorRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("exito",
                    "Proveedor eliminado exitosamente del sistema");
        } catch (EmptyResultDataAccessException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error: El proveedor con ID " + id + " no existe");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al eliminar proveedor: " + e.getMessage());
        }

        return "redirect:/administracion/buscar-proveedor";
    }


    @GetMapping("/editar-proveedor/{id}")
    public String mostrarFormularioEdicion(
            @PathVariable Long id,
            Model model) {

        Optional<ProveedorEntity> proveedor = proveedorRepository.findById(id);

        if (proveedor.isEmpty()) {
            model.addAttribute("error", "Proveedor no encontrado");
            return "redirect:/administracion/buscar-proveedor";
        }

        model.addAttribute("proveedor", proveedor.get());
        model.addAttribute("mainContent", "proveedor/editar_proveedor");
        return "common/layout";
    }

    @PostMapping("/editar-proveedor/{id}")
    public String actualizarProveedor(
            @PathVariable Long id,
            @ModelAttribute ProveedorEntity proveedorActualizado,
            RedirectAttributes redirectAttributes) {

        try {
            ProveedorEntity proveedorExistente = proveedorRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado"));

            // Actualizar solo campos permitidos
            proveedorExistente.setRazonSocial(proveedorActualizado.getRazonSocial());
            proveedorExistente.setRfc(proveedorActualizado.getRfc());
            proveedorExistente.setDireccion(proveedorActualizado.getDireccion());
            proveedorExistente.setCodigoPostal(proveedorActualizado.getCodigoPostal());
            proveedorExistente.setRegimenFiscal(proveedorActualizado.getRegimenFiscal());
            proveedorExistente.setCorreo(proveedorActualizado.getCorreo());
            proveedorExistente.setTelefono(proveedorActualizado.getTelefono());
            proveedorExistente.setContactoNombre(proveedorActualizado.getContactoNombre());
            proveedorExistente.setEstatus(proveedorActualizado.getEstatus());

            proveedorRepository.save(proveedorExistente);

            redirectAttributes.addFlashAttribute("exito", "Proveedor actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
        }

        return "redirect:/administracion/buscar-proveedor";
    }

}
