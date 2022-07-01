package com.example.accusation.usagehistory.domain;

import com.example.accusation.common.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "USAGE_HISTORY")
public class UsageHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "USER_ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @CreatedDate
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Builder
    public UsageHistory(Long userId, UserRole userRole) {
        this.userId = userId;
        this.userRole = userRole;
    }

}
