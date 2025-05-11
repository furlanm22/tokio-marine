package com.tokiomarine.tokiomarine.service;

import com.tokiomarine.tokiomarine.exception.DataTransferenciaInvalidaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaxaServiceTest {

    @InjectMocks
    private TaxaService taxaService;

    @Test
    void deveCalcularTaxaParaTransferenciaNoMesmoDia() {
        LocalDate hoje = LocalDate.now();
        BigDecimal valor = new BigDecimal("1000.00");
        BigDecimal taxa = taxaService.calcularTaxa(hoje, hoje, valor);
        
        // R$3,00 + 2,5% de R$1000,00 = R$3,00 + R$25,00 = R$28,00
        assertEquals(new BigDecimal("28.00"), taxa);
    }

    @Test
    void deveCalcularTaxaFixaParaTransferenciaAteDezDias() {
        LocalDate hoje = LocalDate.now();
        LocalDate amanha = hoje.plusDays(1);
        BigDecimal valor = new BigDecimal("1000.00");
        BigDecimal taxa = taxaService.calcularTaxa(hoje, amanha, valor);
        
        // Taxa fixa de R$12,00
        assertEquals(new BigDecimal("12.00"), taxa);
    }

    @Test
    void deveCalcularTaxaPercentualParaTransferenciaAteVinteDias() {
        LocalDate hoje = LocalDate.now();
        LocalDate quinzeDias = hoje.plusDays(15);
        BigDecimal valor = new BigDecimal("1000.00");
        BigDecimal taxa = taxaService.calcularTaxa(hoje, quinzeDias, valor);
        
        // 8,2% de R$1000,00 = R$82,00
        assertEquals(new BigDecimal("82.00"), taxa);
    }

    @Test
    void deveLancarExcecaoParaDataTransferenciaAnterior() {
        LocalDate hoje = LocalDate.now();
        LocalDate ontem = hoje.minusDays(1);
        BigDecimal valor = new BigDecimal("1000.00");

        assertThrows(DataTransferenciaInvalidaException.class, () -> 
            taxaService.calcularTaxa(hoje, ontem, valor)
        );
    }

    @Test
    void deveLancarExcecaoParaDataTransferenciaSuperiorACinquentaDias() {
        LocalDate hoje = LocalDate.now();
        LocalDate cinquentaEUmDias = hoje.plusDays(51);
        BigDecimal valor = new BigDecimal("1000.00");

        assertThrows(DataTransferenciaInvalidaException.class, () -> 
            taxaService.calcularTaxa(hoje, cinquentaEUmDias, valor)
        );
    }

    @Test
    void deveLancarExcecaoParaDataAgendamentoNula() {
        LocalDate hoje = LocalDate.now();
        BigDecimal valor = new BigDecimal("1000.00");

        assertThrows(DataTransferenciaInvalidaException.class, () -> 
            taxaService.calcularTaxa(null, hoje, valor)
        );
    }

    @Test
    void deveLancarExcecaoParaDataTransferenciaNula() {
        LocalDate hoje = LocalDate.now();
        BigDecimal valor = new BigDecimal("1000.00");

        assertThrows(DataTransferenciaInvalidaException.class, () -> 
            taxaService.calcularTaxa(hoje, null, valor)
        );
    }
} 