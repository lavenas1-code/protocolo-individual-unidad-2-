package com.empresa.nomina.domain.policies;

import com.empresa.nomina.domain.model.entities.Empleado;
import com.empresa.nomina.domain.model.valueobjects.Dinero;

// Patrón Strategy para reglas fiscales variables (porcentaje fijo vs tramos progresivos)
public interface PoliticaDeduccion {
    String nombreConcepto();
    Dinero calcularDeduccion(Dinero baseDevengada, Empleado empleado);
}
