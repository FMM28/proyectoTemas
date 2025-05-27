package mx.unam.aragon.elZorro.controller.administracion.empleado;

import mx.unam.aragon.elZorro.model.entity.EmpleadoEntity;
import mx.unam.aragon.elZorro.service.empleado.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administracion/empleado")
@PreAuthorize("hasRole('ADMIN')")
public class EmpleadoListaController {

    private static final int PAGE_SIZE = 10; // Empleados por página

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/lista")
    public String listarEmpleados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String success,
            Model model) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("apellidoPaterno").ascending());
        Page<EmpleadoEntity> empleadoPage;

        if (search != null && !search.isEmpty()) {
            empleadoPage = empleadoService.buscarPorNombre(search, pageable);
        } else {
            empleadoPage = empleadoService.findAllPaginated(pageable);
        }

        model.addAttribute("empleados", empleadoPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", empleadoPage.getTotalPages());
        model.addAttribute("totalEmpleados", empleadoPage.getTotalElements());
        model.addAttribute("pageSize", PAGE_SIZE);

        // Añadir mensaje de éxito si existe
        if (success != null) {
            model.addAttribute("success", success);
        }

        model.addAttribute("mainContent", "empleado/listado_empleados");
        return "common/layout";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleEmpleado(@PathVariable Long id, Model model) {
        EmpleadoEntity empleado = empleadoService.findById(id);
        model.addAttribute("empleado", empleado);
        model.addAttribute("mainContent", "empleado/detalle_empleado");
        return "common/layout";
    }

}