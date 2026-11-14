package com.empresa.nomina.domain;

import com.empresa.nomina.domain.exceptions.DominioException;
import com.empresa.nomina.domain.model.valueobjects.Dinero;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DineroValueObjectTest {

    @Test
    void debeRechazarMontosNegativosAlCrear() {
        assertThrows(DominioException.class, () -> Dinero.de(-100.0, "EUR"));
    }

    @Test
    void debeSumarYVerificarIgualdadPorValor() {
        Dinero base = Dinero.de(2000.00, "EUR");
        Dinero bono = Dinero.de(350.50, "EUR");
        Dinero total = base.sumar(bono);

        assertEquals(Dinero.de(2350.50, "EUR"), total);
        assertThrows(DominioException.class, () -> base.sumar(Dinero.de(100, "USD")));
    }
}
