package com.tokiomarine.service;

import com.tokiomarine.dto.TransferenciaRequestDTO;
import com.tokiomarine.dto.TransferenciaResponseDTO;
import com.tokiomarine.exception.DadosInvalidosException;
import com.tokiomarine.mapper.TransferenciaMapper;
import com.tokiomarine.model.Transferencia;
import com.tokiomarine.repository.TransferenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository repository;

    @Mock
    private TaxaService taxaService;

    @Mock
    private TransferenciaMapper mapper;

    @InjectMocks
    private TransferenciaService transferenciaService;

    private TransferenciaRequestDTO requestDTO;
    private Transferencia transferencia;
    private TransferenciaResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new TransferenciaRequestDTO();
        requestDTO.setContaOrigem("123456");
        requestDTO.setContaDestino("654321");
        requestDTO.setValor(new BigDecimal("100.00"));
        requestDTO.setDataTransferencia(LocalDateTime.now().plusDays(1));

        transferencia = new Transferencia();
        transferencia.setId(1L);
        transferencia.setContaOrigem(requestDTO.getContaOrigem());
        transferencia.setContaDestino(requestDTO.getContaDestino());
        transferencia.setValor(requestDTO.getValor());
        transferencia.setDataTransferencia(requestDTO.getDataTransferencia());
        transferencia.setDataAgendamento(LocalDateTime.now());

        responseDTO = new TransferenciaResponseDTO();
        responseDTO.setId(transferencia.getId());
        responseDTO.setContaOrigem(transferencia.getContaOrigem());
        responseDTO.setContaDestino(transferencia.getContaDestino());
        responseDTO.setValor(transferencia.getValor());
        responseDTO.setDataTransferencia(transferencia.getDataTransferencia());
        responseDTO.setDataAgendamento(transferencia.getDataAgendamento());
    }

    @Test
    void deveAgendarTransferencia() {
        when(mapper.toEntity(any(TransferenciaRequestDTO.class))).thenReturn(transferencia);
        when(taxaService.calcularTaxa(any(), any(), any())).thenReturn(new BigDecimal("5.00"));
        when(repository.save(any(Transferencia.class))).thenReturn(transferencia);
        when(mapper.toResponseDTO(any(Transferencia.class))).thenReturn(responseDTO);

        TransferenciaResponseDTO resultado = transferenciaService.agendarTransferencia(requestDTO);

        assertNotNull(resultado);
        assertEquals(requestDTO.getContaOrigem(), resultado.getContaOrigem());
        assertEquals(requestDTO.getContaDestino(), resultado.getContaDestino());
        assertEquals(requestDTO.getValor(), resultado.getValor());
        verify(repository).save(any(Transferencia.class));
    }

    @Test
    void deveLancarExcecaoQuandoContasIguais() {
        requestDTO.setContaDestino(requestDTO.getContaOrigem());
        when(mapper.toEntity(any(TransferenciaRequestDTO.class))).thenReturn(transferencia);

        assertThrows(DadosInvalidosException.class, () -> 
            transferenciaService.agendarTransferencia(requestDTO)
        );
    }

    @Test
    void deveLancarExcecaoQuandoDataTransferenciaPassada() {
        requestDTO.setDataTransferencia(LocalDateTime.now().minusDays(1));
        when(mapper.toEntity(any(TransferenciaRequestDTO.class))).thenReturn(transferencia);

        assertThrows(DadosInvalidosException.class, () -> 
            transferenciaService.agendarTransferencia(requestDTO)
        );
    }

    @Test
    void deveListarTransferencias() {
        List<Transferencia> transferencias = Arrays.asList(transferencia);
        List<TransferenciaResponseDTO> responseDTOs = Arrays.asList(responseDTO);

        when(repository.findAllByOrderByDataTransferenciaAsc()).thenReturn(transferencias);
        when(mapper.toResponseDTO(any(Transferencia.class))).thenReturn(responseDTO);

        List<TransferenciaResponseDTO> resultado = transferenciaService.listarTransferencias();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(repository).findAllByOrderByDataTransferenciaAsc();
    }

    @Test
    void deveBuscarTransferenciaPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(transferencia));
        when(mapper.toResponseDTO(any(Transferencia.class))).thenReturn(responseDTO);

        TransferenciaResponseDTO resultado = transferenciaService.buscarTransferencia(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(transferencia.getContaOrigem(), resultado.getContaOrigem());
        assertEquals(transferencia.getContaDestino(), resultado.getContaDestino());
    }

    @Test
    void deveLancarExcecaoTransferenciaNaoEncontrada() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(DadosInvalidosException.class, () -> 
            transferenciaService.buscarTransferencia(999L)
        );
    }
} 