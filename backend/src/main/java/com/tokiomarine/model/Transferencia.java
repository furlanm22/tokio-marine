package com.tokiomarine.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.tokiomarine.exception.DadosInvalidosException;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.tokiomarine.model.base.BaseEntity;

/**
 * Entidade que representa uma transferência no sistema.
 * Contém informações sobre a conta de origem, conta de destino,
 * valor, taxa e datas de agendamento e transferência.
 */
@Entity
@Table(name = "transferencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transferencia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A conta de origem é obrigatória")
    @Column(name = "conta_origem", nullable = false)
    private String contaOrigem;

    @NotBlank(message = "A conta de destino é obrigatória")
    @Column(name = "conta_destino", nullable = false)
    private String contaDestino;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @NotNull(message = "A taxa é obrigatória")
    @Positive(message = "A taxa deve ser maior que zero")
    @Column(name = "taxa", nullable = false)
    private BigDecimal taxa;

    @NotNull(message = "A data de transferência é obrigatória")
    @Column(name = "data_transferencia", nullable = false)
    private LocalDateTime dataTransferencia;

    @NotNull(message = "A data de agendamento é obrigatória")
    @Column(name = "data_agendamento", nullable = false)
    private LocalDateTime dataAgendamento;

    /**
     * Define a data de agendamento como a data atual se não estiver definida.
     */
    @PrePersist
    public void prePersist() {
        if (dataAgendamento == null) {
            dataAgendamento = LocalDateTime.now();
        }
    }

    /**
     * Valida todos os dados da transferência.
     * @throws DadosInvalidosException se algum dado estiver inválido
     */
    public void validar() {
        validarContas();
        validarValor();
        validarDataTransferencia();
    }

    /**
     * Valida as contas de origem e destino.
     * @throws DadosInvalidosException se as contas estiverem inválidas
     */
    private void validarContas() {
        if (contaOrigem == null || contaOrigem.trim().isEmpty()) {
            throw new DadosInvalidosException("Conta de origem é obrigatória");
        }
        
        if (contaDestino == null || contaDestino.trim().isEmpty()) {
            throw new DadosInvalidosException("Conta de destino é obrigatória");
        }
        
        if (contaOrigem.equals(contaDestino)) {
            throw new DadosInvalidosException("Conta de origem não pode ser igual à conta de destino");
        }
    }

    /**
     * Valida o valor da transferência.
     * @throws DadosInvalidosException se o valor estiver inválido
     */
    private void validarValor() {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DadosInvalidosException("Valor deve ser maior que zero");
        }
    }

    /**
     * Valida a data de transferência.
     * @throws DadosInvalidosException se a data estiver inválida
     */
    private void validarDataTransferencia() {
        if (dataTransferencia == null) {
            throw new DadosInvalidosException("Data de transferência é obrigatória");
        }
        LocalDateTime agora = LocalDateTime.now();
        if (dataTransferencia.isBefore(agora)) {
            throw new DadosInvalidosException("Data de transferência não pode ser anterior à data atual");
        }
    }

    /**
     * Calcula o valor total da transferência (valor + taxa).
     * @return o valor total da transferência
     */
    public BigDecimal calcularValorTotal() {
        return valor.add(taxa);
    }
} 