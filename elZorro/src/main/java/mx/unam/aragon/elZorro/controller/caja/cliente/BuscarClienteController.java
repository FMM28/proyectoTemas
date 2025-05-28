package mx.unam.aragon.elZorro.controller.caja.cliente;
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
public class BuscarClienteController {
    ClienteService clienteService;

    @GetMapping("/buscar-cliente")
    public String mostrarBusquedaCliente(Model model) {
        model.addAttribute("mainContent", "cliente/buscar_cliente");
        return "common/layout";
    }

    @PostMapping(value = "/buscar-cliente", params = {"correo", "telefono"} )
    public String buscarCliente(
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String telefono,
            Model model) {

        if ((correo == null || correo.isEmpty()) && (telefono == null || telefono.isEmpty())) {
            model.addAttribute("error", "Debe proporcionar al menos un criterio de búsqueda");
            model.addAttribute("mainContent", "cliente/buscar_cliente");
            return "common/layout";
        }

        Optional<ClienteEntity> cliente;
        if (correo != null && !correo.isEmpty()) {
            cliente = clienteService.findByCorreo(correo);
        } else {
            cliente = clienteService.findByTelefono(telefono);
        }

        if (cliente.isPresent()) {
            model.addAttribute("clienteEncontrado", cliente.get());
            model.addAttribute("exito", "Cliente encontrado");
        } else {
            model.addAttribute("error", "No se encontró ningún cliente con los datos proporcionados");
        }

        model.addAttribute("mainContent", "cliente/buscar_cliente");
        return "common/layout";
    }
}
