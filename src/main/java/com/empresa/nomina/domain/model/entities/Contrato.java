package com.empresa.nomina.domain.model.entities;

import com.empresa.nomina.domain.exceptions.DominioException;
import com.empresa.nomina.domain.model.valueobjects.Dinero;
import com.empresa.nomina.domain.model.valueobjects.Porcentaje;
import com.empresa.nomina.domain.model.valueobjects.RangoFechas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Contrato {
    private final UUID id;
    private final RangoFechas vigencia;
    private String categoria;
    private Dinero sueldoBaseCategoria;
    private final String puestoDestino;
    private final Porcentaje porcentajeJornada;
    private final boolean enPeriodoPrueba;
    private final List<Dinero> complementosPuesto;

    public Contrato(UUID id, RangoFechas vigencia, String categoria, Dinero sueldoBaseCategoria,
                    String puestoDestino, Porcentaje porcentajeJornada, boolean enPeriodoPrueba) {
        if (categoria == null || categoria.isBlank() || puestoDestino == null || puestoDestino.isBlank()) {
            throw new DominioException("La categoría y el puesto de destino son obligatorios.");
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.vigencia = vigencia;
        this.categoria = categoria;
        this.sueldoBaseCategoria = sueldoBaseCategoria;
        this.puestoDestino = puestoDestino;
        this.porcentajeJornada = porcentajeJornada;
        this.enPeriodoPrueba = enPeriodoPrueba;
        this.complementosPuesto = new ArrayList<>();
    }

    public void agregarComplementoPuesto(Dinero complemento) {
        this.complementosPuesto.add(complemento);
    }

    void actualizarCategoria(String nuevaCategoria, Dinero nuevoSueldoBase) {
        if (nuevaCategoria == null || nuevaCategoria.isBlank()) {
            throw new DominioException("La nueva categoría no puede estar vacía.");
        }
        this.categoria = nuevaCategoria;
        this.sueldoBaseCategoria = nuevoSueldoBase;
    }

    public boolean estaActivoEn(LocalDate fecha) {
        return vigencia.estaActivoEn(fecha);
    }

    public Dinero calcularSueldoBaseProporcional() {
        return porcentajeJornada.aplicarA(sueldoBaseCategoria);
    }

    public UUID getId() { return id; }
    public RangoFechas getVigencia() { return vigencia; }
    public String getCategoria() { return categoria; }
    public Dinero getSueldoBaseCategoria() { return sueldoBaseCategoria; }
    public String getPuestoDestino() { return puestoDestino; }
    public Porcentaje getPorcentajeJornada() { return porcentajeJornada; }
    public boolean isEnPeriodoPrueba() { return enPeriodoPrueba; }
    public List<Dinero> getComplementosPuesto() { return Collections.unmodifiableList(complementosPuesto); }
}
