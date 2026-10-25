package es.unex.mdai.omerta.controller;

import es.unex.mdai.omerta.model.NegocioLegal;
import es.unex.mdai.omerta.service.NegocioLegalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/negocios")
public class NegocioLegalController {

    private final NegocioLegalService negocioService;
    private final String nombreOrganizacion;
    private final String fbiTipoBusca;
    private final int incrementoRiesgo;

    public NegocioLegalController(NegocioLegalService negocioService,
                                  @Value("${omerta.nombre:OMERTÀ App}") String nombreOrganizacion,
                                  @Value("${omerta.fbi.tipo-objetivo:RESTAURANTE}") String fbiTipoBusca,
                                  @Value("${omerta.fbi.incremento-riesgo:2}") int incrementoRiesgo) {
        this.negocioService = negocioService;
        this.nombreOrganizacion = nombreOrganizacion;
        this.fbiTipoBusca = fbiTipoBusca;
        this.incrementoRiesgo = incrementoRiesgo;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String tipo, Model model) {
        boolean filtrar = tipo != null && !tipo.isBlank();
        List<NegocioLegal> lista = filtrar
                ? negocioService.buscarPorTipo(tipo)
                : negocioService.obtenerTodos();

        model.addAttribute("nombreOrganizacion", nombreOrganizacion);
        model.addAttribute("negocios", lista);
        model.addAttribute("tipos", negocioService.obtenerTiposUnicos());
        model.addAttribute("tipoSeleccionado", filtrar ? tipo : null);
        model.addAttribute("fbiTipoBusca", fbiTipoBusca);
        model.addAttribute("incrementoRiesgo", incrementoRiesgo);

        return "omerta";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("negocio", new NegocioLegal());
        model.addAttribute("nombreOrganizacion", nombreOrganizacion);
        model.addAttribute("tipos", negocioService.obtenerTiposUnicos());
        return "nuevo-negocio";
    }

    @PostMapping("/nuevo")
    public String guardarNuevo(@ModelAttribute NegocioLegal negocio,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        // Validaciones básicas
        if (negocio.getIngresosLegales() < 0 || negocio.getDineroBlanqueado() < 0) {
            model.addAttribute("error", "Los ingresos y el dinero blanqueado no pueden ser negativos.");
            model.addAttribute("nombreOrganizacion", nombreOrganizacion);
            model.addAttribute("tipos", negocioService.obtenerTiposUnicos());
            return "nuevo-negocio";
        }

        if (negocio.getLvlRiesgoFBI() < 1 || negocio.getLvlRiesgoFBI() > 10) {
            model.addAttribute("error", "El riesgo FBI debe estar entre 1 y 10.");
            model.addAttribute("nombreOrganizacion", nombreOrganizacion);
            model.addAttribute("tipos", negocioService.obtenerTiposUnicos());
            return "nuevo-negocio";
        }

        NegocioLegal guardado = negocioService.añadir(negocio);
        redirectAttributes.addFlashAttribute("mensaje",
                "¡Negocio '" + guardado.getNombre() + "' registrado correctamente (ID: " + guardado.getId() + ")!");

        return "redirect:/negocios";
    }
}