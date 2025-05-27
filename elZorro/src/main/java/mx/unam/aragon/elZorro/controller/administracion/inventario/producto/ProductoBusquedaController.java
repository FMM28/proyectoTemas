package mx.unam.aragon.elZorro.controller.administracion.inventario.producto;

import mx.unam.aragon.elZorro.model.entity.ProductoEntity;
import mx.unam.aragon.elZorro.service.producto.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/administracion/inventario/producto")
@PreAuthorize("hasRole('ADMIN')")
public class ProductoBusquedaController {

    private static final int PAGE_SIZE = 10; // Productos por página

    @Autowired
    private ProductoService productoService;

    @GetMapping("/lista")
    public String listarProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by("nombre").ascending());
        Page<ProductoEntity> productoPage;

        if (search != null && !search.isEmpty()) {
            productoPage = productoService.buscarPorNombre(search, pageable);
        } else {
            productoPage = productoService.findAllPaginated(pageable);
        }

        model.addAttribute("productos", productoPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productoPage.getTotalPages());
        model.addAttribute("totalProductos", productoPage.getTotalElements());
        model.addAttribute("pageSize", PAGE_SIZE);

        model.addAttribute("mainContent", "inventario/listado_Productos");
        return "common/layout";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleProducto(@PathVariable Long id, Model model) {
        ProductoEntity producto = productoService.findById(id);
        System.out.println(producto);
        model.addAttribute("producto", producto);
        model.addAttribute("mainContent", "inventario/detalle_producto");
        return "common/layout";
    }
}