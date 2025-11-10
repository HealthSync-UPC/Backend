package pe.healthsync.meditrack.accesscontrol.domain.model.entities;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.healthsync.meditrack.iam.domain.model.aggregates.User;
import pe.healthsync.meditrack.shared.domain.model.entities.AuditableModel;

@Entity
@NoArgsConstructor
@Getter
public class AccessLog extends AuditableModel {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private boolean accessGranted;

    private Instant accessTime = Instant.now();

    public AccessLog(User user, boolean accessGranted) {
        this.user = user;
        this.accessGranted = accessGranted;
    }
}
