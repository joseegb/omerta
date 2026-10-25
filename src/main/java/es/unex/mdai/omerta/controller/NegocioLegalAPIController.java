package es.unex.mdai.omerta.controller;

import es.unex.mdai.omerta.model.NegocioLegal;
import es.unex.mdai.omerta.service.NegocioLegalService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/negocios")
public class NegocioLegalAPIController {

    private final NegocioLegalService negocioService;

    public NegocioLegalAPIController(NegocioLegalService negocioService) {
        this.negocioService = negocioService;
    }

    @GetMapping
    public List<NegocioLegal> listar(@RequestParam(required = false) String tipo) {
        if (tipo != null && !tipo.isBlank()) {
            return negocioService.buscarPorTipo(tipo);
        }
        return negocioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public NegocioLegal obtenerPorId(@PathVariable Long id) {
        return negocioService.obtenerPorId(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Negocio no encontrado con ID: " + id));
    }

    @GetMapping("/tipo/{tipo}")
    public List<NegocioLegal> buscarPorTipoEnRuta(@PathVariable String tipo) {
        return negocioService.buscarPorTipo(tipo);
    }

    @GetMapping("/riesgo")
    public List<NegocioLegal> buscarPorRiesgoMinimo(@RequestParam(defaultValue = "5") int min) {
        return negocioService.buscarPorRiesgoMinimo(min);
    }
}