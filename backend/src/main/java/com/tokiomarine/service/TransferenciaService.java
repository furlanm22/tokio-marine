package com.tokiomarine.service;

import com.tokiomarine.dto.TransferenciaRequestDTO;
import com.tokiomarine.dto.TransferenciaResponseDTO;
import com.tokiomarine.exception.DadosInvalidosException;
import com.tokiomarine.mapper.TransferenciaMapper;
import com.tokiomarine.model.Transferencia;
import com.tokiomarine.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository repository;
    private final TaxaService taxaService;
    private final TransferenciaMapper mapper;

    private static final String CACHE_TRANSFERENCIAS = "transferencias";
    private static final String CACHE_TRANSFERENCIA = "transferencia";
    private static final ZoneId ZONE_ID = ZoneId.of("America/Sao_Paulo");

    @Transactional
    @CacheEvict(value = {CACHE_TRANSFERENCIAS, CACHE_TRANSFERENCIA}, allEntries = true)
    public TransferenciaResponseDTO agendarTransferencia(TransferenciaRequestDTO requestDTO) {
        try {
            log.info("Agendando transferência: {}", requestDTO);
            
            Transferencia transferencia = mapper.toEntity(requestDTO);
            transferencia.setDataAgendamento(LocalDateTime.now(ZONE_ID));
            
            validarTransferencia(transferencia);
            calcularTaxa(transferencia);
            
            transferencia = repository.save(transferencia);
            log.info("Transferência agendada com sucesso: {}", transferencia);
            
            return mapper.toResponseDTO(transferencia);
        } catch (Exception e) {
            log.error("Erro ao agendar transferência: {}", e.getMessage(), e);
            throw new DadosInvalidosException("Erro ao agendar transferência: " + e.getMessage());
        }
    }

    @Cacheable(value = CACHE_TRANSFERENCIAS, unless = "#result.isEmpty()")
    public List<TransferenciaResponseDTO> listarTransferencias() {
        log.info("Listando todas as transferências");
        return repository.findAllByOrderByDataTransferenciaAsc()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = CACHE_TRANSFERENCIA, key = "#id")
    public TransferenciaResponseDTO buscarTransferencia(Long id) {
        log.info("Buscando transferência por ID: {}", id);
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new DadosInvalidosException("Transferência não encontrada"));
    }

    private void validarTransferencia(Transferencia transferencia) {
        log.info("Validando dados da transferência");
        transferencia.validar();
    }

    private void calcularTaxa(Transferencia transferencia) {
        log.info("Calculando taxa para valor: {}, data agendamento: {}, data transferência: {}", 
            transferencia.getValor(), 
            transferencia.getDataAgendamento(), 
            transferencia.getDataTransferencia());
        
        BigDecimal taxa = taxaService.calcularTaxa(
            transferencia.getValor(),
            transferencia.getDataAgendamento(),
            transferencia.getDataTransferencia()
        );
        
        transferencia.setTaxa(taxa);
    }
} 