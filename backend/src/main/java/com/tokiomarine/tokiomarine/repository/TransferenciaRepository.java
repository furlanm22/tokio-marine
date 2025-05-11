package com.tokiomarine.tokiomarine.repository;

import com.tokiomarine.tokiomarine.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
} 