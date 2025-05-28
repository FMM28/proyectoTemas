package mx.unam.aragon.elZorro.controller.caja;

import jakarta.validation.Valid;
import mx.unam.aragon.elZorro.config.exception.ResourceNotFoundException;
import mx.unam.aragon.elZorro.model.entity.ClienteEntity;
import mx.unam.aragon.elZorro.service.cliente.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/caja")
@PreAuthorize("hasRole('CAJA')")
public class CajaController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("")
    public String caja(Model model) {
        model.addAttribute("mainContent", "caja/home");
        return "common/layout";
    }
}