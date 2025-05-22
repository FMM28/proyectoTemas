package mx.unam.aragon.elZorro.controller.administracion;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administracion")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @GetMapping("")
    public String administracion(Model model) {
        model.addAttribute("mainContent","admin/home");
        return "common/layout";
    }
}
