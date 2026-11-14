package com.empresa.nomina.domain.model.valueobjects;

import com.empresa.nomina.domain.exceptions.DominioException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

public final class PeriodoNomina {
    private final int anio;
    private final int mes;

    public PeriodoNomina(int anio, int mes) {
        if (anio < 2000 || mes < 1 || mes > 12) {
            throw new DominioException("Periodo de nómina inválido.");
        }
        this.anio = anio;
        this.mes = mes;
    }

    public LocalDate fechaInicio() {
        return LocalDate.of(anio, mes, 1);
    }

    public LocalDate fechaFin() {
        return YearMonth.of(anio, mes).atEndOfMonth();
    }

    public int getAnio() { return anio; }
    public int getMes() { return mes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeriodoNomina that)) return false;
        return anio == that.anio && mes == that.mes;
    }

    @Override
    public int hashCode() { return Objects.hash(anio, mes); }
}
