package com.empresa.nomina.domain.model.entities;

import com.empresa.nomina.domain.exceptions.DominioException;
import com.empresa.nomina.domain.model.valueobjects.Dinero;
import com.empresa.nomina.domain.model.valueobjects.RangoFechas;
import java.time.LocalDate;
import java.util.UUID;

public class OcupacionCargo {
    private final UUID id;
    private final String nombreCargo;
    private final RangoFechas periodoOcupacion;
    private final Dinero complementoCargo;
    private final boolean esJefaturaOGerencia;

    public OcupacionCargo(String nombreCargo, RangoFechas periodoOcupacion, Dinero complementoCargo, boolean esJefaturaOGerencia) {
        if (nombreCargo == null || nombreCargo.isBlank()) {
            throw new DominioException("El nombre del cargo es obligatorio.");
        }
        this.id = UUID.randomUUID();
        this.nombreCargo = nombreCargo;
        this.periodoOcupacion = periodoOcupacion;
        this.complementoCargo = complementoCargo;
        this.esJefaturaOGerencia = esJefaturaOGerencia;
    }

    public boolean estaVigenteEn(LocalDate fecha) {
        return periodoOcupacion.estaActivoEn(fecha);
    }

    public UUID getId() { return id; }
    public String getNombreCargo() { return nombreCargo; }
    public RangoFechas getPeriodoOcupacion() { return periodoOcupacion; }
    public Dinero getComplementoCargo() { return complementoCargo; }
    public boolean isEsJefaturaOGerencia() { return esJefaturaOGerencia; }
}
