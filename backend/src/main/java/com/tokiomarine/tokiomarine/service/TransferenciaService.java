package com.tokiomarine.tokiomarine.service;

import com.tokiomarine.tokiomarine.dto.TransferenciaDTO;
import com.tokiomarine.tokiomarine.model.Transferencia;
import com.tokiomarine.tokiomarine.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final TaxaService taxaService;

    @Transactional
    public TransferenciaDTO agendarTransferencia(TransferenciaDTO dto) {
        // Calcula a taxa com base nas datas
        BigDecimal taxa = taxaService.calcularTaxa(
            dto.getDataAgendamento(),
            dto.getDataTransferencia(),
            dto.getValor()
        );
        dto.setTaxa(taxa);

        // Converte DTO para entidade
        Transferencia transferencia = new Transferencia();
        transferencia.setContaOrigem(dto.getContaOrigem());
        transferencia.setContaDestino(dto.getContaDestino());
        transferencia.setValor(dto.getValor());
        transferencia.setTaxa(dto.getTaxa());
        transferencia.setDataTransferencia(dto.getDataTransferencia());
        transferencia.setDataAgendamento(dto.getDataAgendamento());

        // Salva no banco
        transferencia = transferenciaRepository.save(transferencia);
        
        // Atualiza o ID no DTO
        dto.setId(transferencia.getId());
        
        return dto;
    }
} 