package com.tokiomarine.tokiomarine.service;

import com.tokiomarine.tokiomarine.exception.DataTransferenciaInvalidaException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class TaxaService {

    private static final BigDecimal TAXA_FIXA_DIA_ZERO = new BigDecimal("3.00");
    private static final BigDecimal PERCENTUAL_DIA_ZERO = new BigDecimal("0.025");
    private static final BigDecimal TAXA_FIXA_ATE_DEZ_DIAS = new BigDecimal("12.00");
    private static final BigDecimal PERCENTUAL_ATE_VINTE_DIAS = new BigDecimal("0.082");
    private static final BigDecimal PERCENTUAL_ATE_TRINTA_DIAS = new BigDecimal("0.069");
    private static final BigDecimal PERCENTUAL_ATE_QUARENTA_DIAS = new BigDecimal("0.047");
    private static final BigDecimal PERCENTUAL_ATE_CINQUENTA_DIAS = new BigDecimal("0.017");

    public BigDecimal calcularTaxa(LocalDate dataAgendamento, LocalDate dataTransferencia, BigDecimal valor) {
        validarDatas(dataAgendamento, dataTransferencia);
        
        long dias = ChronoUnit.DAYS.between(dataAgendamento, dataTransferencia);
        
        if (dias < 0) {
            throw new DataTransferenciaInvalidaException("A data de transferência não pode ser anterior à data de agendamento");
        }

        BigDecimal taxa;
        if (dias == 0) {
            taxa = TAXA_FIXA_DIA_ZERO.add(valor.multiply(PERCENTUAL_DIA_ZERO));
        } else if (dias <= 10) {
            taxa = TAXA_FIXA_ATE_DEZ_DIAS;
        } else if (dias <= 20) {
            taxa = valor.multiply(PERCENTUAL_ATE_VINTE_DIAS);
        } else if (dias <= 30) {
            taxa = valor.multiply(PERCENTUAL_ATE_TRINTA_DIAS);
        } else if (dias <= 40) {
            taxa = valor.multiply(PERCENTUAL_ATE_QUARENTA_DIAS);
        } else if (dias <= 50) {
            taxa = valor.multiply(PERCENTUAL_ATE_CINQUENTA_DIAS);
        } else {
            throw new DataTransferenciaInvalidaException("A data de transferência não pode ser superior a 50 dias da data de agendamento");
        }

        return taxa.setScale(2, RoundingMode.HALF_UP);
    }

    private void validarDatas(LocalDate dataAgendamento, LocalDate dataTransferencia) {
        if (dataAgendamento == null) {
            throw new DataTransferenciaInvalidaException("A data de agendamento é obrigatória");
        }
        if (dataTransferencia == null) {
            throw new DataTransferenciaInvalidaException("A data de transferência é obrigatória");
        }
    }
} 