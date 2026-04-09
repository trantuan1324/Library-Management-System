package com.rabbyte.librarymanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

@Entity
@Table(
        name = "book_reviews",
        uniqueConstraints = @UniqueConstraint(name = "uk_book_review_user_book", columnNames = {"user_id", "book_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SoftDelete(strategy = SoftDeleteType.TIMESTAMP, columnName = "deleted_at")
public class BookReview extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

    private byte rating;
    private String comment;
}
