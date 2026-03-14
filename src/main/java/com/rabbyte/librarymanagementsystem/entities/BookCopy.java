package com.rabbyte.librarymanagementsystem.entities;

import com.rabbyte.librarymanagementsystem.utils.enums.BookCopyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_copies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "bar_code", length = 10, nullable = false, unique = true)
    private String barCode;

    @Enumerated(EnumType.STRING)
    private BookCopyStatus status;

    @Column(name = "location_shelf", length = 10)
    private String locationShelf;

    @Column(name = "book_condition", length = 255)
    private String bookCondition;
    private Double price;

    @PrePersist
    public void onCreate() {
        this.status = BookCopyStatus.AVAILABLE;
    }
}

