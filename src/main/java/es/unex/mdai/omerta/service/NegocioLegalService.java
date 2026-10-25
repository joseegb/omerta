package es.unex.mdai.omerta.service;

import es.unex.mdai.omerta.model.NegocioLegal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NegocioLegalService {

    private final List<NegocioLegal> negocios = new CopyOnWriteArrayList<>();
    private final String fbiTipoBusca;
    private final int incrementoRiesgo;

    public NegocioLegalService(@Value("${omerta.fbi.tipo-objetivo:RESTAURANTE}") String fbiTipoBusca,
                               @Value("${omerta.fbi.incremento-riesgo:2}") int incrementoRiesgo) {
        this.fbiTipoBusca = fbiTipoBusca;
        this.incrementoRiesgo = incrementoRiesgo;

        // Datos iniciales
        añadir(new NegocioLegal(null, "Lavandería La Pulcra", "LAVANDERIA", "Tony Soprano", 12000.0, 48000.0, 3, true));
        añadir(new NegocioLegal(null, "Pizzería Don Corleone", "RESTAURANTE", "Vito C.", 25000.0, 95000.0, 7, true));
        añadir(new NegocioLegal(null, "Construcciones El Cimiento", "CONSTRUCCION", "Frankie Carbone", 180000.0, 420000.0, 8, true));
        añadir(new NegocioLegal(null, "Club Nocturno El Terciopelo", "NIGHTCLUB", "Silvio Dante", 35000.0, 150000.0, 9, true));
        añadir(new NegocioLegal(null, "Importaciones El Silencio", "IMPORT_EXPORT", "Paulie Gualtieri", 210000.0, 310000.0, 5, true));
        añadir(new NegocioLegal(null, "Talleres El Pistón", "TALLER", "Christopher M.", 15000.0, 20000.0, 2, true));
        añadir(new NegocioLegal(null, "Galería de Arte El Pincel", "ARTE", "Carmela S.", 8000.0, 110000.0, 6, true));
        añadir(new NegocioLegal(null, "Hotel Mirage", "HOSTELERIA", "Salvatore T.", 90000.0, 20000.0, 4, false));
        añadir(new NegocioLegal(null, "Carnicería Satriale", "ALIMENTACION", "Tony Soprano", 18000.0, 35000.0, 4, true));
        añadir(new NegocioLegal(null, "Trattoria Vesuvio", "RESTAURANTE", "Artie Bucco", 30000.0, 15000.0, 2, true));
        añadir(new NegocioLegal(null, "Casino El Dorado", "CASINO", "Silvio Dante", 120000.0, 500000.0, 8, true));
        añadir(new NegocioLegal(null, "Lavandería SúperLimpio", "LAVANDERIA", "Paulie Gualtieri", 5000.0, 60000.0, 9, false));
    }

    public List<String> obtenerTiposUnicos() {
        return negocios.stream()
                .map(NegocioLegal::getTipo)
                .distinct()
                .sorted()
                .toList();
    }

    private void aplicarRiesgoFBI(NegocioLegal negocio) {
        if (negocio.getTipo() != null && negocio.getTipo().equalsIgnoreCase(fbiTipoBusca)) {
            int nuevoRiesgo = negocio.getLvlRiesgoFBI() + incrementoRiesgo;
            if (nuevoRiesgo > 10) {
                nuevoRiesgo = 10;
                negocio.setActivo(false);
            }
            negocio.setLvlRiesgoFBI(nuevoRiesgo);
        }
    }

    public List<NegocioLegal> obtenerTodos() {
        return List.copyOf(negocios);
    }

    public Optional<NegocioLegal> obtenerPorId(Long id) {
        return negocios.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst();
    }

    public List<NegocioLegal> buscarPorTipo(String tipo) {
        return negocios.stream()
                .filter(n -> n.getTipo().equalsIgnoreCase(tipo))
                .toList();
    }

    public List<NegocioLegal> buscarPorRiesgoMinimo(int riesgoMinimo) {
        return negocios.stream()
                .filter(n -> n.getLvlRiesgoFBI() >= riesgoMinimo)
                .toList();
    }

    public synchronized NegocioLegal añadir(NegocioLegal nuevo) {
        if (nuevo.getId() == null) {
            long siguienteId = negocios.stream()
                    .mapToLong(NegocioLegal::getId)
                    .max()
                    .orElse(0) + 1;
            nuevo.setId(siguienteId);
        }
        aplicarRiesgoFBI(nuevo);
        negocios.add(nuevo);
        return nuevo;
    }
}