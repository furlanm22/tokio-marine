package com.tokiomarine.service;

import com.tokiomarine.exception.TaxaInvalidaException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaxaService {
    
    private final Map<Integer, RegraTaxa> regrasTaxa;
    private static final Logger log = LoggerFactory.getLogger(TaxaService.class);
    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");
    private static final int DIAS_MAXIMOS = 50;

    public TaxaService() {
        this.regrasTaxa = new HashMap<>();
        inicializarRegras();
    }

    private void inicializarRegras() {
        regrasTaxa.put(0, new TaxaMesmoDia());
        regrasTaxa.put(10, new TaxaAteDezDias());
        regrasTaxa.put(20, new TaxaAteVinteDias());
        regrasTaxa.put(30, new TaxaAteTrintaDias());
        regrasTaxa.put(40, new TaxaAteQuarentaDias());
        regrasTaxa.put(50, new TaxaAteCinquentaDias());
    }

    public BigDecimal calcularTaxa(BigDecimal valor, LocalDateTime dataAgendamento, LocalDateTime dataTransferencia) {
        try {
            log.info("Iniciando cálculo de taxa");
            log.info("Valor da transferência: R$ {}", valor);
            log.info("Data e hora de agendamento: {}", dataAgendamento);
            log.info("Data e hora de transferência: {}", dataTransferencia);
            
            if (valor == null) {
                throw new TaxaInvalidaException("O valor da transferência não pode ser nulo");
            }
            
            if (dataAgendamento == null) {
                throw new TaxaInvalidaException("A data de agendamento não pode ser nula");
            }
            
            if (dataTransferencia == null) {
                throw new TaxaInvalidaException("A data de transferência não pode ser nula");
            }
            
            // Convertendo para o fuso horário correto
            LocalDateTime dataAgendamentoAjustada = dataAgendamento.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZONE_ID)
                .toLocalDateTime();
            
            LocalDateTime dataTransferenciaAjustada = dataTransferencia.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZONE_ID)
                .toLocalDateTime();
            
            // Calculando a diferença em horas
            long horas = ChronoUnit.HOURS.between(dataAgendamentoAjustada, dataTransferenciaAjustada);
            long dias = horas / 24;
            
            log.info("Cálculo de tempo entre agendamento e transferência:");
            log.info("Data e hora agendamento: {}", dataAgendamentoAjustada);
            log.info("Data e hora transferência: {}", dataTransferenciaAjustada);
            log.info("Total de horas: {}", horas);
            log.info("Total de dias: {}", dias);
            
            if (horas < 0) {
                throw new TaxaInvalidaException("A data de transferência não pode ser anterior à data de agendamento.");
            }

            // Validando o limite máximo de dias
            if (dias > DIAS_MAXIMOS) {
                throw new TaxaInvalidaException(
                    String.format("A data de transferência deve estar dentro de %d dias a partir da data de agendamento. " +
                                "Data de agendamento: %s, Data de transferência: %s, Diferença: %d dias",
                                DIAS_MAXIMOS,
                                dataAgendamentoAjustada.toLocalDate(),
                                dataTransferenciaAjustada.toLocalDate(),
                                dias)
                );
            }

            // Se for no mesmo dia (menos de 24 horas), usa a regra do mesmo dia
            if (horas < 24) {
                RegraTaxa regra = regrasTaxa.get(0);
                log.info("Usando regra de taxa do mesmo dia (menos de 24 horas)");
                BigDecimal taxa = regra.calcularTaxa(valor).setScale(2, RoundingMode.HALF_UP);
                log.info("Taxa calculada: R$ {}", taxa);
                return taxa;
            }

            RegraTaxa regra = encontrarRegraTaxa(dias);
            log.info("Regra de taxa selecionada para {} dias: {}", dias, regra.getClass().getSimpleName());
            
            if (regra == null) {
                throw new TaxaInvalidaException("Não foi possível encontrar uma regra de taxa para o período especificado.");
            }

            BigDecimal taxa = regra.calcularTaxa(valor).setScale(2, RoundingMode.HALF_UP);
            log.info("Taxa calculada: R$ {}", taxa);
            return taxa;
        } catch (Exception e) {
            log.error("Erro ao calcular taxa: {}", e.getMessage(), e);
            throw e;
        }
    }

    private RegraTaxa encontrarRegraTaxa(long dias) {
        if (dias <= 10) {
            return regrasTaxa.get(10);
        } else if (dias <= 20) {
            return regrasTaxa.get(20);
        } else if (dias <= 30) {
            return regrasTaxa.get(30);
        } else if (dias <= 40) {
            return regrasTaxa.get(40);
        } else if (dias <= 50) {
            return regrasTaxa.get(50);
        }
        return null;
    }
}

interface RegraTaxa {
    BigDecimal calcularTaxa(BigDecimal valor);
}

class TaxaMesmoDia implements RegraTaxa {
    @Override
    public BigDecimal calcularTaxa(BigDecimal valor) {
        return new BigDecimal("3.00").add(valor.multiply(new BigDecimal("0.025")));
    }
}

class TaxaAteDezDias implements RegraTaxa {
    @Override
    public BigDecimal calcularTaxa(BigDecimal valor) {
        return new BigDecimal("12.00");
    }
}

class TaxaAteVinteDias implements RegraTaxa {
    @Override
    public BigDecimal calcularTaxa(BigDecimal valor) {
        return valor.multiply(new BigDecimal("0.082"));
    }
}

class TaxaAteTrintaDias implements RegraTaxa {
    @Override
    public BigDecimal calcularTaxa(BigDecimal valor) {
        return valor.multiply(new BigDecimal("0.069"));
    }
}

class TaxaAteQuarentaDias implements RegraTaxa {
    @Override
    public BigDecimal calcularTaxa(BigDecimal valor) {
        return valor.multiply(new BigDecimal("0.047"));
    }
}

class TaxaAteCinquentaDias implements RegraTaxa {
    @Override
    public BigDecimal calcularTaxa(BigDecimal valor) {
        return valor.multiply(new BigDecimal("0.017"));
    }
} 