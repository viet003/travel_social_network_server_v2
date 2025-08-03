package api.v2.travel_social_network_server.entities;

import api.v2.travel_social_network_server.utilities.ProviderEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_credentials", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"provider", "providerUserId"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "credential_id", nullable = false)
    private UUID credentialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 50, nullable = false)
    private ProviderEnum provider; // 'local', 'google', etc.

    @Column(name = "provider_user_id", length = 255)
    private String providerUserId; // Google sub ID, etc.

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
