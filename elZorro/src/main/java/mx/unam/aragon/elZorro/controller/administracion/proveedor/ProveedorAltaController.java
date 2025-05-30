package mx.unam.aragon.elZorro.controller.administracion.proveedor;

import jakarta.validation.Valid;
import mx.unam.aragon.elZorro.converter.UpperConverter;
import mx.unam.aragon.elZorro.model.entity.ProveedorEntity;
import mx.unam.aragon.elZorro.model.enums.EstatusProveedor;
import mx.unam.aragon.elZorro.model.enums.RegimenFiscal;
import mx.unam.aragon.elZorro.service.proveedor.ProveedorService;
import mx.unam.aragon.elZorro.validator.ProveedorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador especializado en el alta de proveedores
 * Responsabilidad: Manejo del formulario de creación de nuevos proveedores
 */
@Controller
@RequestMapping("/administracion/proveedor")
@PreAuthorize("hasRole('ADMIN')")
public class ProveedorAltaController {

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private ProveedorValidator proveedorValidator;

    @GetMapping("/alta")
    public String mostrarFormularioAlta(Model model) {
        model.addAttribute("proveedor", new ProveedorEntity());
        model.addAttribute("regimenes", RegimenFiscal.values());
        model.addAttribute("mainContent", "proveedor/alta_proveedor");
        return "common/layout";
    }

    @PostMapping("/alta")
    public String guardarProveedor(@Valid @ModelAttribute(value = "proveedor") ProveedorEntity proveedor,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model
                                   ) {

        if (result.hasErrors()) {
            return mostrarFormularioConErrores(proveedor, model, result);
        }

        try {
            proveedor.setEstatus(EstatusProveedor.ACTIVO);
            proveedorService.save(proveedor);

            redirectAttributes.addFlashAttribute("exito", "Proveedor guardado exitosamente");
            return "redirect:/administracion/proveedor/alta";

        } catch (Exception e) {
            return mostrarFormularioConError(proveedor, model, "Error al guardar el proveedor: " + e.getMessage());
        }
    }

    private String mostrarFormularioConErrores(ProveedorEntity proveedor, Model model, BindingResult result) {
        System.out.println(result.getAllErrors());
        return mostrarFormularioConDatos(proveedor, model);
    }

    private String mostrarFormularioConError(ProveedorEntity proveedor, Model model, String errorMessage) {
        model.addAttribute("error", errorMessage);
        return mostrarFormularioConDatos(proveedor, model);
    }

    private String mostrarFormularioConDatos(ProveedorEntity proveedor, Model model) {
        model.addAttribute("proveedor", proveedor);
        model.addAttribute("regimenes", RegimenFiscal.values());
        model.addAttribute("mainContent", "proveedor/alta_proveedor");
        return "common/layout";
    }

    @InitBinder("proveedor")
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class,"rfc", new UpperConverter());
        binder.addValidators(proveedorValidator);
    }
}