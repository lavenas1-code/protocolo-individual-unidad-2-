package com.empresa.nomina.domain.events;

import com.empresa.nomina.domain.model.valueobjects.Dinero;
import com.empresa.nomina.domain.model.valueobjects.PeriodoNomina;
import java.time.Instant;
import java.util.UUID;

public record NominaAprobadaYEmidaEvent(
        UUID eventoId,
        Instant ocurridoEn,
        UUID nominaId,
        UUID empleadoId,
        PeriodoNomina periodo,
        Dinero netoAPagar
) implements EventoDominio {
    public NominaAprobadaYEmidaEvent(UUID nominaId, UUID empleadoId, PeriodoNomina periodo, Dinero netoAPagar) {
        this(UUID.randomUUID(), Instant.now(), nominaId, empleadoId, periodo, netoAPagar);
    }
}
