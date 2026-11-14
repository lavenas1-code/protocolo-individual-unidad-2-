package com.empresa.nomina.domain.model.valueobjects;

import com.empresa.nomina.domain.exceptions.DominioException;
import java.util.Objects;

public final class CuentaBancaria {
    private final String numeroCuenta;

    public CuentaBancaria(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.replaceAll("\s+", "").length() < 10) {
            throw new DominioException("El número de cuenta corriente es inválido.");
        }
        this.numeroCuenta = numeroCuenta.replaceAll("\s+", "").toUpperCase();
    }

    public String getNumeroCuenta() { return numeroCuenta; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CuentaBancaria that)) return false;
        return numeroCuenta.equals(that.numeroCuenta);
    }

    @Override
    public int hashCode() { return Objects.hash(numeroCuenta); }
}
