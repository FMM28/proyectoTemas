package mx.unam.aragon.elZorro.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/formulario-recibo")
    @PreAuthorize("hasAnyRole('ADMIN','CAJA')")
    public String mostrarFormulario(Model model) {
        model.addAttribute("numeroRecibo", ""); // Valor inicial vacío
        return "recibo";
    }
}
