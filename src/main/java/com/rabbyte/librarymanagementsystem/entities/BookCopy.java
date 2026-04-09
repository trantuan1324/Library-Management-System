package com.rabbyte.librarymanagementsystem.entities;

import com.rabbyte.librarymanagementsystem.utils.enums.BookCopyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "book_copies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCopy extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "bar_code", length = 50, nullable = false, unique = true)
    private String barCode;

    @Enumerated(EnumType.STRING)
    private BookCopyStatus status;

    @Column(name = "location_shelf", length = 10)
    private String locationShelf;

    @Column(name = "book_condition", length = 255)
    private String bookCondition;
    private Double price;
}

