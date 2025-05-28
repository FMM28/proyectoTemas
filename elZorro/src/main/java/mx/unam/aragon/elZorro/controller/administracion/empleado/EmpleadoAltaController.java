package mx.unam.aragon.elZorro.controller.administracion.empleado;

import jakarta.validation.Valid;
import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import mx.unam.aragon.elZorro.service.empleado.EmpleadoService;
import mx.unam.aragon.elZorro.service.rol.RolService;
import mx.unam.aragon.elZorro.validator.EmpleadoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administracion/empleado")
@PreAuthorize("hasRole('ADMIN')")
public class EmpleadoAltaController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private RolService rolService;

    @Autowired
    private EmpleadoValidator empleadoValidator;

    @GetMapping("/alta")
    public String mostrarFormularioAlta(Model model) {
        model.addAttribute("empleado", new EmpleadoEntity());
        model.addAttribute("roles", rolService.findAll());
        model.addAttribute("mainContent", "empleado/alta_empleado");
        return "common/layout";
    }

    @PostMapping("/alta")
    public String guardarEmpleado(@Valid @ModelAttribute("empleado") EmpleadoEntity empleado,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return mostrarFormularioConErrores(empleado, model, result);
        }

        try {
            // Usamos el método crearEmpleado que incluye el hashing de password
            empleadoService.crearEmpleado(
                    empleado.getNombre(),
                    empleado.getApellidoPaterno(),
                    empleado.getApellidoMaterno(),
                    empleado.getUsuario(),
                    empleado.getPassword(),
                    empleado.getRol()
            );

            redirectAttributes.addFlashAttribute("exito", "Empleado registrado exitosamente");
            return "redirect:/administracion/empleado/alta";

        } catch (Exception e) {
            return mostrarFormularioConError(empleado, model, "Error al guardar el empleado: " + e.getMessage());
        }
    }

    private String mostrarFormularioConErrores(EmpleadoEntity empleado, Model model, BindingResult result) {
        System.out.println(result.getAllErrors());
        return mostrarFormularioConDatos(empleado, model);
    }

    private String mostrarFormularioConError(EmpleadoEntity empleado, Model model, String errorMessage) {
        model.addAttribute("error", errorMessage);
        return mostrarFormularioConDatos(empleado, model);
    }

    private String mostrarFormularioConDatos(EmpleadoEntity empleado, Model model) {
        model.addAttribute("empleado", empleado);
        model.addAttribute("roles", rolService.findAll());
        model.addAttribute("mainContent", "empleado/alta_empleado");
        return "common/layout";
    }

    @InitBinder("empleado")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(empleadoValidator);
    }
}