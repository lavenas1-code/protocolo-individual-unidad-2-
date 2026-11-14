package com.empresa.nomina.domain.policies;

import com.empresa.nomina.domain.model.valueobjects.Dinero;
import java.util.Map;

public class PoliticaTrienio {
    private final int maximoTrieniosPermitidos;
    private final Map<String, Dinero> valorPorCategoria;

    public PoliticaTrienio(int maximoTrieniosPermitidos, Map<String, Dinero> valorPorCategoria) {
        this.maximoTrieniosPermitidos = maximoTrieniosPermitidos;
        this.valorPorCategoria = Map.copyOf(valorPorCategoria);
    }

    public int getMaximoTrieniosPermitidos() {
        return maximoTrieniosPermitidos;
    }

    public Dinero obtenerValorUnitario(String categoria, String moneda) {
        return valorPorCategoria.getOrDefault(categoria, Dinero.cero(moneda));
    }
}
