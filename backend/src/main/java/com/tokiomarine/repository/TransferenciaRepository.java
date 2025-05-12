package com.tokiomarine.repository;

import com.tokiomarine.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByDataTransferenciaGreaterThanEqualOrderByDataTransferenciaAsc(LocalDate data);
    List<Transferencia> findAllByOrderByDataTransferenciaAsc();
} 