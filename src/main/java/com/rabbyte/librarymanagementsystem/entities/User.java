package com.rabbyte.librarymanagementsystem.entities;

import com.rabbyte.librarymanagementsystem.utils.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "`users`",
    indexes = {
            @Index(name = "idx_user_email", columnList = "email"),
            @Index(name = "idx_user_provider", columnList = "provider"),
            @Index(name = "idx_user_provider_id", columnList = "provider_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity{

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AuthProvider provider; // LOCAL, GOOGLE

    @Column(name = "provider_id", length = 100)
    private String providerId; // ID từ Google (sub)

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<BorrowingTransaction> transactions = new HashSet<>();

    @Column(nullable = false)
    private Boolean enabled;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSubscription> subscriptions = new HashSet<>();

    @PrePersist
    public void onCreate() {
        this.enabled = true;

        if (this.provider == null) {
            this.provider = AuthProvider.LOCAL;
        } else {
            this.provider = AuthProvider.valueOf(this.provider.name());
        }
    }

    public User(LocalDateTime createdAt, String email, String password, String fullName, AuthProvider provider, Boolean enabled) {
        super(createdAt);
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.provider = provider;
        this.enabled = enabled;
    }
}
