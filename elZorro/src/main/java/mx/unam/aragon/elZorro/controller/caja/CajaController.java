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
    @PostMapping("/eliminar-cliente")
    public String eliminarCliente(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.deleteById(id);
            redirectAttributes.addFlashAttribute("exito", "Cliente eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el cliente. Intente nuevamente.");
        }
        return "redirect:/caja/buscar-cliente";
    }


    @GetMapping("/editar-cliente/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        ClienteEntity cliente = clienteService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        model.addAttribute("mainContent", "cliente/editar_cliente");
        return "common/layout";
    }

    @PostMapping("/actualizar-cliente/{id}")
    public String actualizarCliente(
            @PathVariable Long id,
            @Valid @ModelAttribute("cliente") ClienteEntity cliente,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("mainContent", "caja/editar_cliente");
            return "common/layout";
        }

        cliente.setId(id); // Asegurar que se mantenga el mismo ID
        clienteService.save(cliente);
        return "redirect:/caja/editar-cliente/" + id + "?exito=true";
    }

    @RequestMapping("/nueva-venta")
    public String nueva_venta(Model model) {
        model.addAttribute("mainContent", "caja/ventas/nueva-venta");
        return "common/layout";
    }
}