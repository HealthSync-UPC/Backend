package pe.healthsync.meditrack.profiles.domain.model.aggregates;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.healthsync.meditrack.iam.domain.model.aggregates.User;
import pe.healthsync.meditrack.profiles.domain.model.commands.CreateProfileCommand;
import pe.healthsync.meditrack.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@NoArgsConstructor
public class Profile extends AuditableAbstractAggregateRoot<Profile> {
    private String firstName;
    private String lastName;
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    private User admin;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public Profile(CreateProfileCommand command) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.position = command.position();
        this.admin = command.admin();
        this.user = command.user();
    }

}
