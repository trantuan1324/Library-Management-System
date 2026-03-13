package com.rabbyte.librarymanagementsystem.entities;

import com.rabbyte.librarymanagementsystem.utils.constants.BorrowStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "borrowing_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "copy_id")
    private BookCopy bookCopy;
    private Instant borrowedAt;
    private Instant returnedAt;
    private BorrowStatus status;
    private Double fineAmount;

    @PrePersist
    public void onCreate() {
        this.borrowedAt = Instant.now();
        this.status = BorrowStatus.Borrowed;
    }
}
