package com.tokiomarine.mapper;

import com.tokiomarine.dto.TransferenciaRequestDTO;
import com.tokiomarine.dto.TransferenciaResponseDTO;
import com.tokiomarine.model.Transferencia;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class TransferenciaMapper {

    public Transferencia toEntity(TransferenciaRequestDTO dto) {
        Transferencia transferencia = new Transferencia();
        transferencia.setContaOrigem(dto.getContaOrigem());
        transferencia.setContaDestino(dto.getContaDestino());
        transferencia.setValor(dto.getValor());
        transferencia.setDataTransferencia(dto.getDataTransferencia());
        return transferencia;
    }

    public TransferenciaResponseDTO toResponseDTO(Transferencia transferencia) {
        TransferenciaResponseDTO dto = new TransferenciaResponseDTO();
        dto.setId(transferencia.getId());
        dto.setContaOrigem(transferencia.getContaOrigem());
        dto.setContaDestino(transferencia.getContaDestino());
        dto.setValor(transferencia.getValor());
        dto.setTaxa(transferencia.getTaxa());
        dto.setDataTransferencia(transferencia.getDataTransferencia());
        dto.setDataAgendamento(transferencia.getDataAgendamento());
        dto.setDataCriacao(transferencia.getDataCriacao());
        dto.setDataAtualizacao(transferencia.getDataAtualizacao());
        return dto;
    }
} 