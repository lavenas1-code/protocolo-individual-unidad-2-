package com.empresa.nomina.domain.specifications;

import com.empresa.nomina.domain.model.entities.Empleado;
import java.time.LocalDate;

public class EmpleadoProximoJubilacionSpec implements Specification<Empleado> {
    private final LocalDate fechaReferencia;
    private final int edadUmbralJubilacion;

    public EmpleadoProximoJubilacionSpec(LocalDate fechaReferencia, int edadUmbralJubilacion) {
        this.fechaReferencia = fechaReferencia;
        this.edadUmbralJubilacion = edadUmbralJubilacion;
    }

    @Override
    public boolean isSatisfiedBy(Empleado empleado) {
        return empleado.calcularEdadEn(fechaReferencia) >= edadUmbralJubilacion;
    }
}
