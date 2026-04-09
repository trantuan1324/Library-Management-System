package com.rabbyte.librarymanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "book_reviews",
        uniqueConstraints = @UniqueConstraint(name = "uk_book_review_user_book", columnNames = {"user_id", "book_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookReview extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private byte rating;
    private String comment;
}
