package mx.unam.aragon.elZorro.controller.caja;

import mx.unam.aragon.elZorro.model.entity.ClienteEntity;
import mx.unam.aragon.elZorro.service.cliente.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/caja")
public class CajaController {

    private final ClienteService clienteService;

    // Inyección de dependencia por constructor (recomendado)
    public CajaController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String mostrarDashboard(Model model) {
        // Datos estáticos para el dashboard
        model.addAttribute("ventasHoy", 15); // Ejemplo: 15 ventas hoy
        model.addAttribute("totalClientes", 42); // Ejemplo: 42 clientes registrados
        model.addAttribute("fechaActual", LocalDate.now());

        // Productos con bajo stock (datos de ejemplo)
        String[] productosBajoStock = {"Café Premium (Quedan 3)", "Azúcar (Quedan 2)", "Vasos desechables (Quedan 4)"};
        model.addAttribute("productosBajoStock", productosBajoStock);

        return "caja/dashboard";
    }

    @GetMapping("/nueva-venta")
    public String iniciarNuevaVenta(Model model) {
        // Datos de ejemplo para el formulario
        String[] productos = {"Café Americano - $25", "Café Expresso - $30", "Capuchino - $35", "Té - $20"};
        String[] metodosPago = {"Efectivo", "Tarjeta de crédito", "Tarjeta de débito"};

        model.addAttribute("productos", productos);
        model.addAttribute("metodosPago", metodosPago);

        return "caja/nueva-venta";
    }

    // CORRECCIÓN: Había un typo en "/alta-cliete"
    @GetMapping("/alta-cliente")
    public String mostrarFormularioCliente(Model model) {
        model.addAttribute("cliente", new ClienteEntity());
        return "/caja/cliente/alta-cliente";
    }

    // CORRECCIÓN: Eliminé el prefijo duplicado "/caja"
    @PostMapping("/alta-cliente")
    public String procesarRegistroCliente(@ModelAttribute("cliente") ClienteEntity cliente,
                                          BindingResult result,
                                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "caja/cliente/alta-cliente";
        }

        clienteService.save(cliente);
        redirectAttributes.addFlashAttribute("success", "Cliente registrado exitosamente!");
        return "redirect:/caja/cliente/alta-cliente";
    }

    @GetMapping("/historial-ventas")
    public String mostrarHistorialVentas(Model model) {
        // Datos de ejemplo para el historial
        String[] ventasHoy = {
                "Venta #1001 - $125.00 - 10:30 AM",
                "Venta #1002 - $75.50 - 11:15 AM",
                "Venta #1003 - $210.00 - 12:45 PM"
        };
        model.addAttribute("ventas", ventasHoy);

        return "caja/historial-ventas";
    }
}