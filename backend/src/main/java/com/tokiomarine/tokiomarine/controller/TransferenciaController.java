package com.tokiomarine.tokiomarine.controller;

import com.tokiomarine.tokiomarine.dto.TransferenciaDTO;
import com.tokiomarine.tokiomarine.service.TransferenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/transferencias")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<TransferenciaDTO> agendarTransferencia(@Valid @RequestBody TransferenciaDTO transferenciaDTO) {
        TransferenciaDTO transferenciaAgendada = transferenciaService.agendarTransferencia(transferenciaDTO);
        return ResponseEntity.ok(transferenciaAgendada);
    }
} 