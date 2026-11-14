package com.empresa.nomina.domain.specifications;

import com.empresa.nomina.domain.model.entities.Empleado;
import java.time.LocalDate;

public class ContratoEnPeriodoDePruebaSpec implements Specification<Empleado> {
    private final LocalDate fechaReferencia;

    public ContratoEnPeriodoDePruebaSpec(LocalDate fechaReferencia) {
        this.fechaReferencia = fechaReferencia;
    }

    @Override
    public boolean isSatisfiedBy(Empleado empleado) {
        return empleado.obtenerContratosActivosEn(fechaReferencia).stream()
                .anyMatch(c -> c.isEnPeriodoPrueba());
    }
}
