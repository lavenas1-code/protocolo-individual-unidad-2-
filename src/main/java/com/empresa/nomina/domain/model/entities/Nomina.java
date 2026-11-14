package com.empresa.nomina.domain.model.entities;

import com.empresa.nomina.domain.events.EventoDominio;
import com.empresa.nomina.domain.events.NominaAprobadaYEmidaEvent;
import com.empresa.nomina.domain.exceptions.DominioException;
import com.empresa.nomina.domain.exceptions.NominaYaEmitidaException;
import com.empresa.nomina.domain.model.valueobjects.CuentaBancaria;
import com.empresa.nomina.domain.model.valueobjects.Dinero;
import com.empresa.nomina.domain.model.valueobjects.PeriodoNomina;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

// Aggregate Root de Nómina: una vez aprobada y emitida es inmutable para auditoría
public class Nomina {
    public enum EstadoNomina { BORRADOR, APROBADA_Y_EMITIDA }

    private final UUID id;
    private final UUID empleadoId;
    private final CuentaBancaria cuentaDestino;
    private final PeriodoNomina periodo;
    private final String moneda;
    private final List<LineaPercepcion> percepciones;
    private final List<LineaDeduccion> deducciones;
    private EstadoNomina estado;
    private final List<EventoDominio> eventosDominio;

    public Nomina(UUID id, UUID empleadoId, CuentaBancaria cuentaDestino, PeriodoNomina periodo, String moneda) {
        this.id = id != null ? id : UUID.randomUUID();
        this.empleadoId = empleadoId;
        this.cuentaDestino = cuentaDestino;
        this.periodo = periodo;
        this.moneda = moneda;
        this.percepciones = new ArrayList<>();
        this.deducciones = new ArrayList<>();
        this.estado = EstadoNomina.BORRADOR;
        this.eventosDominio = new ArrayList<>();
    }

    public void agregarPercepcion(String concepto, Dinero importe) {
        verificarModificable();
        this.percepciones.add(new LineaPercepcion(concepto, importe));
    }

    public void agregarDeduccion(String concepto, Dinero importe) {
        verificarModificable();
        this.deducciones.add(new LineaDeduccion(concepto, importe));
    }

    public void aprobarYEmtir() {
        verificarModificable();
        if (percepciones.isEmpty()) {
            throw new DominioException("No se puede emitir una nómina sin percepciones salariales.");
        }
        Dinero neto = calcularNetoAPagar();
        this.estado = EstadoNomina.APROBADA_Y_EMITIDA;
        this.eventosDominio.add(new NominaAprobadaYEmidaEvent(id, empleadoId, periodo, neto));
    }

    private void verificarModificable() {
        if (this.estado == EstadoNomina.APROBADA_Y_EMITIDA) {
            throw new NominaYaEmitidaException("La nómina ya fue emitida y no puede modificarse.");
        }
    }

    public Dinero calcularTotalDevengado() {
        return percepciones.stream()
                .map(LineaPercepcion::importe)
                .reduce(Dinero.cero(moneda), Dinero::sumar);
    }

    public Dinero calcularTotalDeducciones() {
        return deducciones.stream()
                .map(LineaDeduccion::importe)
                .reduce(Dinero.cero(moneda), Dinero::sumar);
    }

    public Dinero calcularNetoAPagar() {
        return calcularTotalDevengado().restar(calcularTotalDeducciones());
    }

    public List<EventoDominio> extraerEventos() {
        List<EventoDominio> copia = List.copyOf(eventosDominio);
        eventosDominio.clear();
        return copia;
    }

    public UUID getId() { return id; }
    public UUID getEmpleadoId() { return empleadoId; }
    public CuentaBancaria getCuentaDestino() { return cuentaDestino; }
    public PeriodoNomina getPeriodo() { return periodo; }
    public EstadoNomina getEstado() { return estado; }
    public List<LineaPercepcion> getPercepciones() { return Collections.unmodifiableList(percepciones); }
    public List<LineaDeduccion> getDeducciones() { return Collections.unmodifiableList(deducciones); }
}
