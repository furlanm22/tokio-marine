package com.tokiomarine.tokiomarine.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransferenciaDTO {

    private Long id;

    @NotBlank(message = "A conta de origem é obrigatória")
    @Size(min = 10, max = 10, message = "A conta de origem deve ter 10 dígitos")
    @Pattern(regexp = "^\\d{10}$", message = "A conta de origem deve conter apenas números")
    private String contaOrigem;

    @NotBlank(message = "A conta de destino é obrigatória")
    @Size(min = 10, max = 10, message = "A conta de destino deve ter 10 dígitos")
    @Pattern(regexp = "^\\d{10}$", message = "A conta de destino deve conter apenas números")
    private String contaDestino;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "O valor deve ter no máximo 10 dígitos inteiros e 2 decimais")
    private BigDecimal valor;

    @NotNull(message = "A taxa é obrigatória")
    @DecimalMin(value = "0.00", message = "A taxa não pode ser negativa")
    @Digits(integer = 10, fraction = 2, message = "A taxa deve ter no máximo 10 dígitos inteiros e 2 decimais")
    private BigDecimal taxa;

    @NotNull(message = "A data de transferência é obrigatória")
    private LocalDate dataTransferencia;

    @NotNull(message = "A data de agendamento é obrigatória")
    private LocalDate dataAgendamento;
} 