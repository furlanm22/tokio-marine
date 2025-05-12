package com.tokiomarine.controller;

import com.tokiomarine.dto.TransferenciaRequestDTO;
import com.tokiomarine.dto.TransferenciaResponseDTO;
import com.tokiomarine.service.TransferenciaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api/transferencias")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;
    private static final CacheControl NO_CACHE = CacheControl.noCache();

    @PostMapping
    public ResponseEntity<TransferenciaResponseDTO> agendarTransferencia(
            @Valid @RequestBody TransferenciaRequestDTO request) {
        log.info("Recebendo requisição para agendar transferência: {}", request);
        TransferenciaResponseDTO response = transferenciaService.agendarTransferencia(request);
        log.info("Transferência agendada com sucesso: {}", response);
        return ResponseEntity.ok()
                .cacheControl(NO_CACHE)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransferenciaResponseDTO>> listarTransferencias() {
        log.info("Recebendo requisição para listar transferências");
        List<TransferenciaResponseDTO> transferencias = transferenciaService.listarTransferencias();
        log.info("Transferências listadas com sucesso. Total: {}", transferencias.size());
        return ResponseEntity.ok()
                .cacheControl(NO_CACHE)
                .body(transferencias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaResponseDTO> buscarTransferencia(@PathVariable Long id) {
        log.info("Recebendo requisição para buscar transferência por ID: {}", id);
        TransferenciaResponseDTO transferencia = transferenciaService.buscarTransferencia(id);
        return ResponseEntity.ok()
                .cacheControl(NO_CACHE)
                .body(transferencia);
    }
} 