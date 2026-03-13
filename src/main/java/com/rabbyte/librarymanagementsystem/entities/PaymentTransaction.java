package com.rabbyte.librarymanagementsystem.entities;

import com.rabbyte.librarymanagementsystem.utils.constants.PaymentProvider;
import com.rabbyte.librarymanagementsystem.utils.constants.PaymentStatus;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private Double amount;
    private PaymentProvider provider;

    private String externalId;
    private PaymentStatus status;
}
