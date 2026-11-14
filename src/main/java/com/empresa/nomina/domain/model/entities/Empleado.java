package com.empresa.nomina.domain.model.entities;

import com.empresa.nomina.domain.events.CategoriaContractualCambiadaEvent;
import com.empresa.nomina.domain.events.EventoDominio;
import com.empresa.nomina.domain.exceptions.DominioException;
import com.empresa.nomina.domain.exceptions.SolapamientoDeContratosException;
import com.empresa.nomina.domain.model.valueobjects.CuentaBancaria;
import com.empresa.nomina.domain.model.valueobjects.DNI;
import com.empresa.nomina.domain.model.valueobjects.Dinero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

// Aggregate Root que protege las invariantes contractuales y laborales del empleado
public class Empleado {
    private final UUID id;
    private final DNI dni;
    private String nombreCompleto;
    private final LocalDate fechaNacimiento;
    private CuentaBancaria cuentaBancaria;
    private final boolean tieneDiscapacidadReconocida;
    private final List<Contrato> contratos;
    private final List<OcupacionCargo> ocupacionesCargo;
    private final List<EventoDominio> eventosDominio;

    public Empleado(UUID id, DNI dni, String nombreCompleto, LocalDate fechaNacimiento,
                    CuentaBancaria cuentaBancaria, boolean tieneDiscapacidadReconocida) {
        if (dni == null || cuentaBancaria == null || nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new DominioException("Datos obligatorios del empleado incompletos.");
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.cuentaBancaria = cuentaBancaria;
        this.tieneDiscapacidadReconocida = tieneDiscapacidadReconocida;
        this.contratos = new ArrayList<>();
        this.ocupacionesCargo = new ArrayList<>();
        this.eventosDominio = new ArrayList<>();
    }

    // Invariante: la suma de jornadas de contratos simultáneos no puede superar el 100%
    public void registrarContrato(Contrato nuevoContrato) {
        LocalDate fechaEvaluacion = nuevoContrato.getVigencia().getFechaInicio();
        BigDecimal jornadaAcumulada = contratos.stream()
                .filter(c -> c.estaActivoEn(fechaEvaluacion))
                .map(c -> c.getPorcentajeJornada().getValor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (jornadaAcumulada.add(nuevoContrato.getPorcentajeJornada().getValor()).compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new SolapamientoDeContratosException("La jornada acumulada supera el 100% permitido.");
        }
        this.contratos.add(nuevoContrato);
    }

    public void asignarCargo(OcupacionCargo ocupacion) {
        this.ocupacionesCargo.add(ocupacion);
    }

    public void promoverCategoria(UUID contratoId, String nuevaCategoria, Dinero nuevoSueldoBase) {
        Contrato contrato = contratos.stream()
                .filter(c -> c.getId().equals(contratoId))
                .findFirst()
                .orElseThrow(() -> new DominioException("Contrato no encontrado en el empleado."));
        contrato.actualizarCategoria(nuevaCategoria, nuevoSueldoBase);
        this.eventosDominio.add(new CategoriaContractualCambiadaEvent(this.id, contratoId, nuevaCategoria));
    }

    public void actualizarCuentaBancaria(CuentaBancaria nuevaCuenta) {
        if (nuevaCuenta == null) throw new DominioException("La cuenta bancaria no puede ser nula.");
        this.cuentaBancaria = nuevaCuenta;
    }

    public List<Contrato> obtenerContratosActivosEn(LocalDate fecha) {
        return contratos.stream().filter(c -> c.estaActivoEn(fecha)).toList();
    }

    public List<OcupacionCargo> obtenerCargosActivosEn(LocalDate fecha) {
        return ocupacionesCargo.stream().filter(o -> o.estaVigenteEn(fecha)).toList();
    }

    public int calcularEdadEn(LocalDate fecha) {
        return Period.between(fechaNacimiento, fecha).getYears();
    }

    public List<EventoDominio> extraerEventos() {
        List<EventoDominio> copia = List.copyOf(eventosDominio);
        eventosDominio.clear();
        return copia;
    }

    public UUID getId() { return id; }
    public DNI getDni() { return dni; }
    public String getNombreCompleto() { return nombreCompleto; }
    public CuentaBancaria getCuentaBancaria() { return cuentaBancaria; }
    public boolean isTieneDiscapacidadReconocida() { return tieneDiscapacidadReconocida; }
    public List<Contrato> getContratos() { return Collections.unmodifiableList(contratos); }
    public List<OcupacionCargo> getOcupacionesCargo() { return Collections.unmodifiableList(ocupacionesCargo); }
}
