package pe.healthsync.meditrack.accesscontrol.domain.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;
import pe.healthsync.meditrack.accesscontrol.domain.model.valueobjects.AlertType;
import pe.healthsync.meditrack.shared.domain.model.entities.AuditableModel;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Alert extends AuditableModel {
    @ManyToOne(fetch = FetchType.LAZY)
    private Zone zone;

    @Enumerated(EnumType.STRING)
    private AlertType type;
}
