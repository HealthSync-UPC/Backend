package pe.healthsync.meditrack.inventory.domain.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;
import pe.healthsync.meditrack.inventory.domain.model.commands.CreateItemCommand;
import pe.healthsync.meditrack.shared.domain.model.entities.AuditableModel;

@Entity
@Getter
@NoArgsConstructor
public class Item extends AuditableModel {
    private String name;

    private String code;

    private String description;

    private int quantity;

    private String unit;

    private boolean active = true;

    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    private Zone zone;

    public Item(CreateItemCommand command) {
        this.name = command.name();
        this.code = command.code();
        this.description = command.description();
        this.quantity = command.quantity();
        this.unit = command.unit();
        this.location = command.location();
    }
}
