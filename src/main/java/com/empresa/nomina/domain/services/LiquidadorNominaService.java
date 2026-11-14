package com.empresa.nomina.domain.services;

import com.empresa.nomina.domain.exceptions.DominioException;
import com.empresa.nomina.domain.model.entities.Contrato;
import com.empresa.nomina.domain.model.entities.Empleado;
import com.empresa.nomina.domain.model.entities.Nomina;
import com.empresa.nomina.domain.model.entities.OcupacionCargo;
import com.empresa.nomina.domain.model.valueobjects.Dinero;
import com.empresa.nomina.domain.model.valueobjects.PeriodoNomina;
import com.empresa.nomina.domain.policies.PoliticaDeduccion;
import com.empresa.nomina.domain.policies.PoliticaTrienio;
import java.time.LocalDate;
import java.util.List;

// Domain Service principal que orquesta percepciones, trienios, cargos y deducciones
public class LiquidadorNominaService {
    private final CalculadorAntiguedadService calculadorAntiguedad;

    public LiquidadorNominaService(CalculadorAntiguedadService calculadorAntiguedad) {
        this.calculadorAntiguedad = calculadorAntiguedad;
    }

    public Nomina liquidar(Empleado empleado, PeriodoNomina periodo,
                           PoliticaTrienio politicaTrienio, List<PoliticaDeduccion> politicasDeduccion) {
        LocalDate fechaCorte = periodo.fechaFin();
        List<Contrato> contratosActivos = empleado.obtenerContratosActivosEn(fechaCorte);

        if (contratosActivos.isEmpty()) {
            throw new DominioException("El empleado no tiene contratos activos para el periodo indicado.");
        }

        String moneda = contratosActivos.get(0).getSueldoBaseCategoria().getMoneda();
        Nomina nomina = new Nomina(null, empleado.getId(), empleado.getCuentaBancaria(), periodo, moneda);

        for (Contrato contrato : contratosActivos) {
            nomina.agregarPercepcion("Sueldo Base (" + contrato.getCategoria() + ")", contrato.calcularSueldoBaseProporcional());
            for (Dinero compPuesto : contrato.getComplementosPuesto()) {
                nomina.agregarPercepcion("Complemento Puesto: " + contrato.getPuestoDestino(), compPuesto);
            }
        }

        Dinero importeTrienios = calculadorAntiguedad.calcularImporteTrienios(
                empleado, contratosActivos.get(0), fechaCorte, politicaTrienio
        );
        if (importeTrienios.getMonto().signum() > 0) {
            nomina.agregarPercepcion("Complemento Antigüedad (Trienios)", importeTrienios);
        }

        for (OcupacionCargo cargo : empleado.obtenerCargosActivosEn(fechaCorte)) {
            nomina.agregarPercepcion("Complemento Cargo: " + cargo.getNombreCargo(), cargo.getComplementoCargo());
        }

        Dinero totalDevengado = nomina.calcularTotalDevengado();
        for (PoliticaDeduccion politica : politicasDeduccion) {
            Dinero descuento = politica.calcularDeduccion(totalDevengado, empleado);
            if (descuento.getMonto().signum() > 0) {
                nomina.agregarDeduccion(politica.nombreConcepto(), descuento);
            }
        }

        return nomina;
    }
}
