package com.empresa.nomina.domain.model.valueobjects;

import com.empresa.nomina.domain.exceptions.DominioException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public final class RangoFechas {
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;

    public RangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null) {
            throw new DominioException("La fecha de inicio es obligatoria.");
        }
        if (fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new DominioException("La fecha de baja/fin no puede ser anterior a la fecha de alta.");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public boolean estaActivoEn(LocalDate fecha) {
        boolean despuesDeInicio = !fecha.isBefore(fechaInicio);
        boolean antesDeFin = (fechaFin == null) || !fecha.isAfter(fechaFin);
        return despuesDeInicio && antesDeFin;
    }

    public long aniosTranscurridosHasta(LocalDate referencia) {
        LocalDate limite = (fechaFin != null && fechaFin.isBefore(referencia)) ? fechaFin : referencia;
        if (limite.isBefore(fechaInicio)) return 0;
        return ChronoUnit.YEARS.between(fechaInicio, limite);
    }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RangoFechas that)) return false;
        return fechaInicio.equals(that.fechaInicio) && Objects.equals(fechaFin, that.fechaFin);
    }

    @Override
    public int hashCode() { return Objects.hash(fechaInicio, fechaFin); }
}
