package com.empresa.nomina.domain;

import com.empresa.nomina.domain.events.NominaAprobadaYEmidaEvent;
import com.empresa.nomina.domain.exceptions.NominaYaEmitidaException;
import com.empresa.nomina.domain.factories.NominaFactory;
import com.empresa.nomina.domain.model.entities.*;
import com.empresa.nomina.domain.model.valueobjects.*;
import com.empresa.nomina.domain.policies.PoliticaTrienio;
import com.empresa.nomina.domain.specifications.EmpleadoProximoJubilacionSpec;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class LiquidadorNominaServiceTest {

    @Test
    void debeLiquidarNominaConTrieniosCargosYDeduccionesYLuegoBloquearEdicion() {
        Empleado empleado = new Empleado(
                null, new DNI("87654321B"), "Carlos Mendoza",
                LocalDate.of(1963, 3, 10), new CuentaBancaria("ES7921000813610123456789"), false
        );

        Contrato contrato = new Contrato(
                null, new RangoFechas(LocalDate.of(2019, 1, 1), null),
                "CATEGORIA_A", Dinero.de(3000, "EUR"), "Ingeniero Senior", new Porcentaje(100), false
        );
        contrato.agregarComplementoPuesto(Dinero.de(400, "EUR"));
        empleado.registrarContrato(contrato);

        empleado.asignarCargo(new OcupacionCargo(
                "Jefe de Departamento",
                new RangoFechas(LocalDate.of(2024, 1, 1), null),
                Dinero.de(600, "EUR"),
                true
        ));

        PoliticaTrienio politicaTrienio = new PoliticaTrienio(5, Map.of("CATEGORIA_A", Dinero.de(100, "EUR")));
        NominaFactory factory = new NominaFactory();
        Nomina nomina = factory.crearNominaMensualEstandar(empleado, new PeriodoNomina(2026, 11), politicaTrienio);

        // 3000 base + 400 puesto + 200 (2 trienios de 2019 a 2026) + 600 cargo = 4200 devengado
        assertEquals(Dinero.de(4200.00, "EUR"), nomina.calcularTotalDevengado());
        assertTrue(nomina.calcularNetoAPagar().getMonto().doubleValue() > 0);
        assertTrue(new EmpleadoProximoJubilacionSpec(LocalDate.of(2026, 11, 30), 62).isSatisfiedBy(empleado));

        nomina.aprobarYEmtir();
        assertEquals(1, nomina.extraerEventos().size());
        assertThrows(NominaYaEmitidaException.class, () -> nomina.agregarPercepcion("Bono tardío", Dinero.de(100, "EUR")));
    }
}
