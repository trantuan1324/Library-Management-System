package com.rabbyte.librarymanagementsystem.entities;

import com.rabbyte.librarymanagementsystem.utils.enums.PaymentProvider;
import com.rabbyte.librarymanagementsystem.utils.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private Double amount;
    private PaymentProvider provider;

    private String externalId;
    private PaymentStatus status;
}
