package com.tokiomarine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Desculpe, ocorreu um erro inesperado. Nossa equipe técnica foi notificada e está trabalhando para resolver o problema. Por favor, tente novamente em alguns minutos.",
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TaxaInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleTaxaInvalidaException(TaxaInvalidaException e) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Não foi possível calcular a taxa para esta transferência. Verifique se o valor e a data da transferência estão corretos.",
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<ErrorResponse> handleDadosInvalidosException(DadosInvalidosException e) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            
            // Traduzir nomes dos campos
            String fieldNameTranslated = translateFieldName(fieldName);
            
            // Melhorar mensagens de erro
            String improvedMessage = improveErrorMessage(errorMessage, fieldNameTranslated);
            
            errors.put(fieldNameTranslated, improvedMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private String translateFieldName(String fieldName) {
        switch (fieldName) {
            case "contaOrigem":
                return "Conta de Origem";
            case "contaDestino":
                return "Conta de Destino";
            case "valor":
                return "Valor";
            case "dataTransferencia":
                return "Data da Transferência";
            default:
                return fieldName;
        }
    }
    
    private String improveErrorMessage(String errorMessage, String fieldName) {
        if (errorMessage.contains("NotNull") || errorMessage.contains("NotBlank")) {
            return String.format("O campo %s é obrigatório", fieldName);
        }
        if (errorMessage.contains("Size") && fieldName.contains("Conta")) {
            return String.format("A %s deve conter exatamente 6 dígitos", fieldName);
        }
        if (errorMessage.contains("Min")) {
            return String.format("O %s deve ser maior que zero", fieldName);
        }
        if (errorMessage.contains("Future")) {
            return "A data da transferência deve ser futura";
        }
        return errorMessage;
    }
} 