package com.rabbyte.librarymanagementsystem.entities;

import com.rabbyte.librarymanagementsystem.utils.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "user_subscriptions",
        indexes = {
                @Index(name = "idx_user_subscription_user", columnList = "user_id"),
                @Index(name = "idx_user_subscription_status", columnList = "status"),
                @Index(name = "idx_user_subscription_end_date", columnList = "end_date")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SubscriptionStatus status;

    @PrePersist
    public void onCreate() {
        this.startDate = new Date();
        this.status = SubscriptionStatus.ACTIVE;

        if (subscriptionPlan != null && subscriptionPlan.getDurationDays() != null) {
            long durationMillis = subscriptionPlan.getDurationDays() * 86400000L;
            this.endDate = new Date(this.startDate.getTime() + durationMillis);
        }
    }

}
