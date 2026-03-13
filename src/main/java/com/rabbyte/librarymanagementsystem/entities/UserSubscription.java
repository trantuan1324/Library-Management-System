package com.rabbyte.librarymanagementsystem.entities;

import com.rabbyte.librarymanagementsystem.utils.constants.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "user_subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private SubscriptionPlan subscriptionPlan;

    private Date startDate;
    private Date endDate;
    private SubscriptionStatus status;

    @PrePersist
    public void onCreate() {
        this.startDate = new Date();
        this.endDate = new Date(new Date().getTime() + subscriptionPlan.getDurationDays());
    }

}
