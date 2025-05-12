package com.tokiomarine.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransferenciaRequestDTO {
    
    @NotBlank(message = "Conta de origem é obrigatória")
    private String contaOrigem;
    
    @NotBlank(message = "Conta de destino é obrigatória")
    private String contaDestino;
    
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    private BigDecimal valor;
    
    @NotNull(message = "Data de transferência é obrigatória")
    private LocalDateTime dataTransferencia;
} 