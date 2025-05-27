package mx.unam.aragon.elZorro.controller.administracion.inventario.producto;

import jakarta.validation.Valid;
import mx.unam.aragon.elZorro.model.entity.CategoriaEntity;
import mx.unam.aragon.elZorro.service.categoria.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administracion/inventario/producto")
@PreAuthorize("hasRole('ADMIN')")
public class ProductoBusquedaController {

    @GetMapping("/lista")
    public String listarCategorias(Model model) {
        model.addAttribute("mainContent", "inventario/listado_Productos");
        return "common/layout";
    }
}
