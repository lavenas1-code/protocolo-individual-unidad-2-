package com.empresa.nomina.domain.factories;

import com.empresa.nomina.domain.model.entities.Empleado;
import com.empresa.nomina.domain.model.entities.Nomina;
import com.empresa.nomina.domain.model.valueobjects.PeriodoNomina;
import com.empresa.nomina.domain.policies.PoliticaDeduccion;
import com.empresa.nomina.domain.policies.PoliticaDeduccionFija;
import com.empresa.nomina.domain.policies.PoliticaDeduccionIRPFTramos;
import com.empresa.nomina.domain.policies.PoliticaTrienio;
import com.empresa.nomina.domain.model.valueobjects.Porcentaje;
import com.empresa.nomina.domain.services.CalculadorAntiguedadService;
import com.empresa.nomina.domain.services.LiquidadorNominaService;
import java.util.List;

// Patrón Factory para encapsular la creación compleja de una Nómina mensual
public class NominaFactory {
    private final LiquidadorNominaService liquidadorService;

    public NominaFactory() {
        this.liquidadorService = new LiquidadorNominaService(new CalculadorAntiguedadService());
    }

    public Nomina crearNominaMensualEstandar(Empleado empleado, PeriodoNomina periodo, PoliticaTrienio politicaTrienio) {
        List<PoliticaDeduccion> deduccionesLegales = List.of(
                new PoliticaDeduccionFija("Seguridad Social - Contingencias Comunes (4.7%)", new Porcentaje(4.7)),
                new PoliticaDeduccionFija("Desempleo y Formación Profesional (1.65%)", new Porcentaje(1.65)),
                new PoliticaDeduccionIRPFTramos()
        );
        return liquidadorService.liquidar(empleado, periodo, politicaTrienio, deduccionesLegales);
    }
}
