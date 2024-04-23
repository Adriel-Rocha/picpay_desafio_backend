package com.adriel.picpaydesafiobackend.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("TRANSACTIONS")
public record Transaction(@Id Long id,
                        Long payer,
                        Long payee,
                        BigDecimal value,
                        @CreatedDate LocalDateTime createdAT) {

    public Transaction {
        value = value.setScale(2);
    }
    
}
