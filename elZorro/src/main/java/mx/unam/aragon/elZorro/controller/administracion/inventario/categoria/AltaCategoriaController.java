package mx.unam.aragon.elZorro.controller.administracion.inventario.categoria;

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
@RequestMapping("/administracion/inventario/categoria")
@PreAuthorize("hasRole('ADMIN')")
public class AltaCategoriaController{

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/alta")
    public String mostrarFormularioAlta(Model model) {
        if (!model.containsAttribute("categoria")) {
            model.addAttribute("categoria", new CategoriaEntity());
        }
        model.addAttribute("mainContent", "inventario/alta_categoria");
        return "common/layout";
    }

    @PostMapping("/alta")
    public String procesarAltaCategoria(
            @Valid @ModelAttribute("categoria") CategoriaEntity categoria,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoria", result);
            redirectAttributes.addFlashAttribute("categoria", categoria);
            return "redirect:/administracion/inventario/categoria/alta";
        }

        try {
            categoriaService.save(categoria);
            redirectAttributes.addFlashAttribute("success", "Categoría registrada exitosamente");
            return "redirect:/administracion/inventario/categoria/alta";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al guardar la categoría");
            redirectAttributes.addFlashAttribute("categoria", categoria);
            return "redirect:/administracion/inventario/categoria/alta";
        }
    }
}