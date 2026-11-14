package com.empresa.nomina.domain.services;

import com.empresa.nomina.domain.exceptions.LimiteTrieniosExcedidoException;
import com.empresa.nomina.domain.model.entities.Contrato;
import com.empresa.nomina.domain.model.entities.Empleado;
import com.empresa.nomina.domain.model.valueobjects.Dinero;
import com.empresa.nomina.domain.policies.PoliticaTrienio;
import java.math.BigDecimal;
import java.time.LocalDate;

// Domain Service: coordina historial de múltiples contratos y políticas de antigüedad
public class CalculadorAntiguedadService {
    public int calcularNumeroTrienios(Empleado empleado, LocalDate fechaCorte, PoliticaTrienio politica) {
        long aniosTotales = empleado.getContratos().stream()
                .mapToLong(c -> c.getVigencia().aniosTranscurridosHasta(fechaCorte))
                .sum();

        int trieniosCalculados = (int) (aniosTotales / 3);
        if (trieniosCalculados > politica.getMaximoTrieniosPermitidos()) {
            return politica.getMaximoTrieniosPermitidos();
        }
        return trieniosCalculados;
    }

    public Dinero calcularImporteTrienios(Empleado empleado, Contrato contratoActual,
                                          LocalDate fechaCorte, PoliticaTrienio politica) {
        int trienios = calcularNumeroTrienios(empleado, fechaCorte, politica);
        if (trienios < 0) {
            throw new LimiteTrieniosExcedidoException("Cálculo de trienios inválido.");
        }
        Dinero valorUnitario = politica.obtenerValorUnitario(
                contratoActual.getCategoria(),
                contratoActual.getSueldoBaseCategoria().getMoneda()
        );
        return valorUnitario.multiplicar(BigDecimal.valueOf(trienios));
    }
}
