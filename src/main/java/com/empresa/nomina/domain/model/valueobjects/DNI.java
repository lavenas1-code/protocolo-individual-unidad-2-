package com.empresa.nomina.domain.model.valueobjects;

import com.empresa.nomina.domain.exceptions.DominioException;
import java.util.Objects;

public final class DNI {
    private final String valor;

    public DNI(String valor) {
        if (valor == null || !valor.matches("^[0-9]{6,10}[A-Z0-9]?$")) {
            throw new DominioException("El DNI proporcionado no cumple el formato válido.");
        }
        this.valor = valor.trim().toUpperCase();
    }

    public String getValor() { return valor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DNI dni)) return false;
        return valor.equals(dni.valor);
    }

    @Override
    public int hashCode() { return Objects.hash(valor); }
}
