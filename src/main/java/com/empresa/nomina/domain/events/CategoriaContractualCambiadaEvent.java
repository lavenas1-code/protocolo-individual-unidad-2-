package com.empresa.nomina.domain.events;

import java.time.Instant;
import java.util.UUID;

public record CategoriaContractualCambiadaEvent(
        UUID eventoId,
        Instant ocurridoEn,
        UUID empleadoId,
        UUID contratoId,
        String nuevaCategoria
) implements EventoDominio {
    public CategoriaContractualCambiadaEvent(UUID empleadoId, UUID contratoId, String nuevaCategoria) {
        this(UUID.randomUUID(), Instant.now(), empleadoId, contratoId, nuevaCategoria);
    }
}
