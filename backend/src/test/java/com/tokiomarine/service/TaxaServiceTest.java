package com.tokiomarine.service;

import com.tokiomarine.exception.TaxaInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaxaServiceTest {

    private TaxaService taxaService;

    @BeforeEach
    void setUp() {
        taxaService = new TaxaService();
    }

    @Test
    @DisplayName("Deve calcular taxa para transferência no mesmo dia")
    void deveCalcularTaxaMesmoDia() {
        LocalDateTime agora = LocalDateTime.now();
        BigDecimal valor = new BigDecimal("100.00");
        
        BigDecimal taxa = taxaService.calcularTaxa(valor, agora, agora);
        
        assertEquals(new BigDecimal("5.50"), taxa); // R$3,00 + 2,5% de R$100,00
    }

    @Test
    @DisplayName("Deve calcular taxa fixa para transferência até 10 dias")
    void deveCalcularTaxaAteDezDias() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataTransferencia = agora.plusDays(5);
        BigDecimal valor = new BigDecimal("100.00");
        
        BigDecimal taxa = taxaService.calcularTaxa(valor, agora, dataTransferencia);
        
        assertEquals(new BigDecimal("12.00"), taxa);
    }

    @Test
    @DisplayName("Deve calcular taxa de 8,2% para transferência até 20 dias")
    void deveCalcularTaxaAteVinteDias() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataTransferencia = agora.plusDays(15);
        BigDecimal valor = new BigDecimal("100.00");
        
        BigDecimal taxa = taxaService.calcularTaxa(valor, agora, dataTransferencia);
        
        assertEquals(new BigDecimal("8.20"), taxa);
    }

    @Test
    @DisplayName("Deve lançar exceção para data de transferência anterior à data atual")
    void deveLancarExcecaoDataAnterior() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataAnterior = agora.minusDays(1);
        BigDecimal valor = new BigDecimal("100.00");
        
        assertThrows(TaxaInvalidaException.class, () -> 
            taxaService.calcularTaxa(valor, agora, dataAnterior)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção para data de transferência após 50 dias")
    void deveLancarExcecaoDataAposCinquentaDias() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataAposCinquentaDias = agora.plusDays(51);
        BigDecimal valor = new BigDecimal("100.00");
        
        assertThrows(TaxaInvalidaException.class, () -> 
            taxaService.calcularTaxa(valor, agora, dataAposCinquentaDias)
        );
    }
} 