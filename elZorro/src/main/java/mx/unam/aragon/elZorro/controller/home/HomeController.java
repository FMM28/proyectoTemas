package mx.unam.aragon.elZorro.controller.home;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/administracion")
    public String administracion() {
        return "admin/administracion";
    }

    @PreAuthorize("hasRole('CAJA')")
    @GetMapping("/caja")
    public String caja() {
        return "caja/caja";
    }
}
