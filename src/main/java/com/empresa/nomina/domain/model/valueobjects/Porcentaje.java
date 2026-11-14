package com.empresa.nomina.domain.model.valueobjects;

import com.empresa.nomina.domain.exceptions.DominioException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Porcentaje {
    private final BigDecimal valor;

    public Porcentaje(double valor) {
        if (valor < 0 || valor > 100) {
            throw new DominioException("El porcentaje debe estar entre 0 y 100.");
        }
        this.valor = BigDecimal.valueOf(valor);
    }

    public Dinero aplicarA(Dinero base) {
        BigDecimal factor = valor.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        return base.multiplicar(factor);
    }

    public BigDecimal getValor() { return valor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Porcentaje that)) return false;
        return valor.compareTo(that.valor) == 0;
    }

    @Override
    public int hashCode() { return Objects.hash(valor.stripTrailingZeros()); }
}
