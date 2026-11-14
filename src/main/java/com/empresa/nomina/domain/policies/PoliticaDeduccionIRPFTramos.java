package com.empresa.nomina.domain.policies;

import com.empresa.nomina.domain.model.entities.Empleado;
import com.empresa.nomina.domain.model.valueobjects.Dinero;
import com.empresa.nomina.domain.model.valueobjects.Porcentaje;
import java.math.BigDecimal;

// Cálculo por tramos según reglas del caso de estudio + exención por discapacidad
public class PoliticaDeduccionIRPFTramos implements PoliticaDeduccion {
    @Override
    public String nombreConcepto() {
        return "Retención IRPF";
    }

    @Override
    public Dinero calcularDeduccion(Dinero baseDevengada, Empleado empleado) {
        if (empleado.isTieneDiscapacidadReconocida()) {
            return Dinero.cero(baseDevengada.getMoneda());
        }
        BigDecimal ingresoAnualEstimado = baseDevengada.getMonto().multiply(BigDecimal.valueOf(12));
        Porcentaje tasaAplicable;

        if (ingresoAnualEstimado.compareTo(BigDecimal.valueOf(17000)) <= 0) {
            tasaAplicable = new Porcentaje(24.0);
        } else if (ingresoAnualEstimado.compareTo(BigDecimal.valueOf(32000)) <= 0) {
            tasaAplicable = new Porcentaje(28.0);
        } else if (ingresoAnualEstimado.compareTo(BigDecimal.valueOf(52000)) <= 0) {
            tasaAplicable = new Porcentaje(37.0);
        } else {
            tasaAplicable = new Porcentaje(43.0);
        }
        return tasaAplicable.aplicarA(baseDevengada);
    }
}
