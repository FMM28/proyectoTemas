package mx.unam.aragon.elZorro.controller.caja.cliente;
import jakarta.validation.Valid;
import mx.unam.aragon.elZorro.config.exception.ResourceNotFoundException;
import mx.unam.aragon.elZorro.model.entity.ClienteEntity;
import mx.unam.aragon.elZorro.service.cliente.ClienteService;
import mx.unam.aragon.elZorro.validator.ClienteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/caja")
@PreAuthorize("hasRole('CAJA')")
public class AltaClienteController {
    @Autowired
    ClienteService clienteService;

    @Autowired
    ClienteValidator clienteValidator;

    @GetMapping("/alta-cliente")
    public String mostrarFormularioCliente(Model model) {
        model.addAttribute("cliente", new ClienteEntity());
        model.addAttribute("activePage", "caja");
        model.addAttribute("mainContent", "cliente/alta_cliente");
        return "common/layout";
    }

    @PostMapping("/guardar-cliente")
    public String guardarCliente(@Valid @ModelAttribute("cliente") ClienteEntity cliente,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("mainContent", "cliente/alta_cliente");
            return "common/layout";
        }
        clienteService.save(cliente);
        return "redirect:/caja/alta-cliente?exito=true";
    }

    @InitBinder("cliente")
    public void initClienteBinder(WebDataBinder binder) {
        binder.setValidator(clienteValidator);
    }
}
