package mx.unam.aragon.elZorro.controller.caja;

import jakarta.validation.Valid;
import mx.unam.aragon.elZorro.model.entity.ClienteEntity;
import mx.unam.aragon.elZorro.service.cliente.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/alta-cliente")
    public String mostrarFormularioCliente(Model model) {
        model.addAttribute("cliente", new ClienteEntity());
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
}