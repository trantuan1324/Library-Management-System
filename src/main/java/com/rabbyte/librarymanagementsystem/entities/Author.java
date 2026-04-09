package com.rabbyte.librarymanagementsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "bio", length = 255)
    private String bio;

    @Column(name = "nationality", length = 255)
    private String nationality;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();

    @Column(name = "deleted_at")
    private Date deletedAt;

    @PreRemove
    public void onRemove() {
        this.deletedAt = new Date();
    }
}
