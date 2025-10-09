package pe.healthsync.meditrack.profiles.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.healthsync.meditrack.profiles.domain.model.commands.CreateProfileCommand;
import pe.healthsync.meditrack.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@NoArgsConstructor
public class Profile extends AuditableAbstractAggregateRoot<Profile> {
    private String firstName;
    private String lastName;
    private String position;
    private Long adminId;

    public Profile(CreateProfileCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.position = command.position();
        this.adminId = command.adminId();
    }

}
