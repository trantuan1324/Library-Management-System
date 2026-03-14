package com.rabbyte.librarymanagementsystem.entities;

import com.rabbyte.librarymanagementsystem.utils.enums.BorrowStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "borrowing_transactions",
        indexes = {
                @Index(name = "idx_borrowing_user", columnList = "user_id"),
                @Index(name = "idx_borrowing_copy", columnList = "copy_id"),
                @Index(name = "idx_borrowing_status", columnList = "status"),
                @Index(name = "idx_borrowing_returned_at", columnList = "returned_at")
        }
)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copy_id")
    private BookCopy bookCopy;

    @Column(name = "borrowed_at", nullable = false, updatable = false)
    private Date borrowedAt;

    @Column(name = "returned_at")
    private Date returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private BorrowStatus status;

    @Column(name = "fine_amount")
    private Double fineAmount;

    @PrePersist
    public void onCreate() {
        this.borrowedAt = new Date();
        this.status = BorrowStatus.BORROWED;
        this.fineAmount = 0.0;
    }
}
