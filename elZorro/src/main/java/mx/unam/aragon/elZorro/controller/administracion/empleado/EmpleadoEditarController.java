package mx.unam.aragon.elZorro.controller.administracion.empleado;

import jakarta.validation.Valid;
import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import mx.unam.aragon.elZorro.service.empleado.EmpleadoService;
import mx.unam.aragon.elZorro.service.rol.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administracion/empleado")
@PreAuthorize("hasRole('ADMIN')")
public class EmpleadoEditarController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private RolService rolService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        EmpleadoEntity empleado = empleadoService.findById(id);
        model.addAttribute("empleado", empleado);
        model.addAttribute("roles", rolService.findAll());
        model.addAttribute("mainContent", "empleado/editar_empleado");
        return "common/layout";
    }

    @PostMapping("/editar/{id}")
    public String procesarEdicion(
            @PathVariable Long id,
            @Valid @ModelAttribute("empleado") EmpleadoEntity empleado,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return mostrarFormularioConErrores(empleado, model, result);
        }

        try {
            EmpleadoEntity empleadoExistente = empleadoService.findById(id);

            // Actualizar campos editables
            empleadoExistente.setNombre(empleado.getNombre());
            empleadoExistente.setApellidoPaterno(empleado.getApellidoPaterno());
            empleadoExistente.setApellidoMaterno(empleado.getApellidoMaterno());
            empleadoExistente.setUsuario(empleado.getUsuario());
            empleadoExistente.setRol(empleado.getRol());

            // Solo actualizar password si se proporcionó uno nuevo (con hashing)
            if (empleado.getPassword() != null && !empleado.getPassword().isEmpty()) {
                empleadoExistente.setPassword(passwordEncoder.encode(empleado.getPassword()));
            }

            empleadoService.save(empleadoExistente);
            redirectAttributes.addFlashAttribute("exito", "Empleado actualizado exitosamente");
            return "redirect:/administracion/empleado/detalle/" + id;

        } catch (Exception e) {
            return mostrarFormularioConError(empleado, model, "Error al actualizar el empleado: " + e.getMessage());
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            empleadoService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Empleado eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el empleado");
        }
        return "redirect:/administracion/empleado/lista";
    }

    private String mostrarFormularioConErrores(EmpleadoEntity empleado, Model model, BindingResult result) {
        model.addAttribute("roles", rolService.findAll());
        model.addAttribute("mainContent", "empleado/editar_empleado");
        return "common/layout";
    }

    private String mostrarFormularioConError(EmpleadoEntity empleado, Model model, String errorMessage) {
        model.addAttribute("error", errorMessage);
        model.addAttribute("roles", rolService.findAll());
        model.addAttribute("mainContent", "empleado/editar_empleado");
        return "common/layout";
    }
}