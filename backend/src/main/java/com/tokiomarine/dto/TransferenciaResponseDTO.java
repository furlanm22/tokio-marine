package com.tokiomarine.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransferenciaResponseDTO {
    private Long id;
    private String contaOrigem;
    private String contaDestino;
    private BigDecimal valor;
    private BigDecimal taxa;
    private LocalDateTime dataTransferencia;
    private LocalDateTime dataAgendamento;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
} 