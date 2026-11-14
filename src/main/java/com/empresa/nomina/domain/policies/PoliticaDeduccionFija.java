package com.empresa.nomina.domain.policies;

import com.empresa.nomina.domain.model.entities.Empleado;
import com.empresa.nomina.domain.model.valueobjects.Dinero;
import com.empresa.nomina.domain.model.valueobjects.Porcentaje;

public class PoliticaDeduccionFija implements PoliticaDeduccion {
    private final String concepto;
    private final Porcentaje porcentaje;

    public PoliticaDeduccionFija(String concepto, Porcentaje porcentaje) {
        this.concepto = concepto;
        this.porcentaje = porcentaje;
    }

    @Override
    public String nombreConcepto() {
        return concepto;
    }

    @Override
    public Dinero calcularDeduccion(Dinero baseDevengada, Empleado empleado) {
        return porcentaje.aplicarA(baseDevengada);
    }
}
