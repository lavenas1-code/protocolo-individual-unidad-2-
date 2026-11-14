# Capa de Dominio - Sistema de Gestión de Nóminas (DDD)

Implementación de la capa de dominio en **Java 17** aplicando **Domain-Driven Design (DDD)** para el Caso de Estudio Aplicado (CEA) de Nóminas y Recursos Humanos.

## Estructura del Proyecto
- `domain/model/valueobjects`: Objetos de valor inmutables (`Dinero`, `DNI`, `CuentaBancaria`, `PeriodoNomina`, `RangoFechas`, `Porcentaje`).
- `domain/model/entities`: Entidades y Raíces de Agregado (`Empleado`, `Contrato`, `OcupacionCargo`, `Nomina`, `LineaPercepcion`, `LineaDeduccion`).
- `domain/exceptions`: Excepciones de negocio (`DominioException`, `NominaYaEmitidaException`, `SolapamientoDeContratosException`, `LimiteTrieniosExcedidoException`).
- `domain/policies`: Estrategias y políticas fiscales/salariales (`PoliticaDeduccion`, `PoliticaDeduccionFija`, `PoliticaDeduccionIRPFTramos`, `PoliticaTrienio`).
- `domain/services`: Servicios de dominio (`CalculadorAntiguedadService`, `LiquidadorNominaService`).
- `domain/events`: Eventos de dominio (`EventoDominio`, `NominaAprobadaYEmidaEvent`, `CategoriaContractualCambiadaEvent`).
- `domain/specifications`: Patrón Specification (`Specification`, `ContratoEnPeriodoDePruebaSpec`, `EmpleadoProximoJubilacionSpec`).
- `domain/factories`: Patrón Factory (`NominaFactory`).

## Ejecución de Pruebas Unitarias
```bash
mvn test
```
