package mx.unam.aragon.elZorro.controller.administracion.inventario.categoria;

import jakarta.validation.Valid;
import mx.unam.aragon.elZorro.model.entity.CategoriaEntity;
import mx.unam.aragon.elZorro.service.categoria.CategoriaService;
import mx.unam.aragon.elZorro.validator.CategoriaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administracion/inventario/categoria")
@PreAuthorize("hasRole('ADMIN')")
public class ManageCategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaValidator categoriaValidator;

    @GetMapping("/lista")
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        model.addAttribute("mainContent", "inventario/listado_categorias");
        return "common/layout";
    }

    // Vista para mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormEdicion(@PathVariable Long id, Model model) {
        CategoriaEntity categoria = categoriaService.findById(id);
        if (categoria == null) {
            model.addAttribute("error", "Categoría no encontrada");
            return "redirect:/administracion/inventario/categoria/lista";
        }
        model.addAttribute("categoria", categoria);
        model.addAttribute("mainContent", "inventario/editar_categoria");
        return "common/layout";
    }

    // Procesar actualización
    @PostMapping("/actualizar")
    public String actualizarCategoria(
            @Valid @ModelAttribute("categoria") CategoriaEntity categoria,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoria", result);
            redirectAttributes.addFlashAttribute("categoria", categoria);
            return "redirect:/administracion/inventario/categoria/editar/" + categoria.getId();
        }

        try {
            categoriaService.save(categoria);
            redirectAttributes.addFlashAttribute("success", "Categoría actualizada exitosamente");
            return "redirect:/administracion/inventario/categoria/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "redirect:/administracion/inventario/categoria/editar/" + categoria.getId();
        }
    }

    // Eliminar categoría
    @PostMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Categoría eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/administracion/inventario/categoria/lista";
    }

    @InitBinder("categoria")
    public void initBinderCategoria(WebDataBinder binder) {
        binder.addValidators(categoriaValidator);
    }
}