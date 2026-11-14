package com.empresa.nomina.domain.model.valueobjects;

import com.empresa.nomina.domain.exceptions.DominioException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

// Value Object inmutable para operaciones monetarias sin pérdida de precisión
public final class Dinero {
    private final BigDecimal monto;
    private final String moneda;

    public Dinero(BigDecimal monto, String moneda) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) < 0) {
            throw new DominioException("El monto monetario no puede ser nulo ni negativo.");
        }
        if (moneda == null || moneda.isBlank()) {
            throw new DominioException("La moneda es obligatoria.");
        }
        this.monto = monto.setScale(2, RoundingMode.HALF_UP);
        this.moneda = moneda.toUpperCase();
    }

    public static Dinero de(double monto, String moneda) {
        return new Dinero(BigDecimal.valueOf(monto), moneda);
    }

    public static Dinero cero(String moneda) {
        return new Dinero(BigDecimal.ZERO, moneda);
    }

    public Dinero sumar(Dinero otro) {
        validarMismaMoneda(otro);
        return new Dinero(this.monto.add(otro.monto), this.moneda);
    }

    public Dinero restar(Dinero otro) {
        validarMismaMoneda(otro);
        if (this.monto.compareTo(otro.monto) < 0) {
            throw new DominioException("El resultado de la resta monetaria no puede ser negativo.");
        }
        return new Dinero(this.monto.subtract(otro.monto), this.moneda);
    }

    public Dinero multiplicar(BigDecimal factor) {
        return new Dinero(this.monto.multiply(factor), this.moneda);
    }

    private void validarMismaMoneda(Dinero otro) {
        if (otro == null || !this.moneda.equals(otro.moneda)) {
            throw new DominioException("No se pueden operar montos de distinta moneda.");
        }
    }

    public BigDecimal getMonto() { return monto; }
    public String getMoneda() { return moneda; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dinero dinero)) return false;
        return monto.compareTo(dinero.monto) == 0 && moneda.equals(dinero.moneda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monto.stripTrailingZeros(), moneda);
    }
}
