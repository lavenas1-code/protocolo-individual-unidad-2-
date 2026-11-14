package com.empresa.nomina.domain;

import com.empresa.nomina.domain.exceptions.SolapamientoDeContratosException;
import com.empresa.nomina.domain.model.entities.Contrato;
import com.empresa.nomina.domain.model.entities.Empleado;
import com.empresa.nomina.domain.model.valueobjects.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class EmpleadoAgregadoTest {

    @Test
    void debePermitirDosContratosMedioTiempoSimultaneosPeroRechazarExcesoDeJornada() {
        Empleado empleado = new Empleado(
                null, new DNI("12345678A"), "Laura Gómez",
                LocalDate.of(1990, 5, 12), new CuentaBancaria("ES9121000418450200051332"), false
        );

        Contrato medioTiempo1 = new Contrato(
                null, new RangoFechas(LocalDate.of(2025, 1, 1), null),
                "SENIOR", Dinero.de(3000, "EUR"), "Arquitecto Backend", new Porcentaje(50), false
        );
        Contrato medioTiempo2 = new Contrato(
                null, new RangoFechas(LocalDate.of(2025, 2, 1), null),
                "SENIOR", Dinero.de(3000, "EUR"), "Líder Técnico", new Porcentaje(50), false
        );
        Contrato contratoExtra = new Contrato(
                null, new RangoFechas(LocalDate.of(2025, 3, 1), null),
                "JUNIOR", Dinero.de(1500, "EUR"), "Soporte", new Porcentaje(25), true
        );

        empleado.registrarContrato(medioTiempo1);
        empleado.registrarContrato(medioTiempo2);

        assertEquals(2, empleado.obtenerContratosActivosEn(LocalDate.of(2025, 4, 1)).size());
        assertThrows(SolapamientoDeContratosException.class, () -> empleado.registrarContrato(contratoExtra));
    }
}
