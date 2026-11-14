package com.empresa.nomina.domain.model.entities;

import com.empresa.nomina.domain.model.valueobjects.Dinero;

public record LineaPercepcion(String concepto, Dinero importe) {
}
