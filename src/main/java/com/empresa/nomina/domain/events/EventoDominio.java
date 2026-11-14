package com.empresa.nomina.domain.events;

import java.time.Instant;
import java.util.UUID;

public interface EventoDominio {
    UUID eventoId();
    Instant ocurridoEn();
}
