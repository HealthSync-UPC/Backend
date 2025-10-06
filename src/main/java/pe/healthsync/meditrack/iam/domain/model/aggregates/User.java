package pe.healthsync.meditrack.iam.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.healthsync.meditrack.iam.domain.model.commands.SignUpCommand;
import pe.healthsync.meditrack.iam.domain.model.valueobjects.Roles;
import pe.healthsync.meditrack.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@NoArgsConstructor
public class User extends AuditableAbstractAggregateRoot<User> {
    private String email;

    private String password;

    private String organizationName;

    private String twoFactorSecret;

    private boolean isTwoFactorEnabled;

    private Roles role;

    public User(SignUpCommand command, String twoFactorSecret) {
        this.email = command.email();
        this.password = command.password();
        this.organizationName = command.organizationName();
        this.twoFactorSecret = twoFactorSecret;
        this.isTwoFactorEnabled = false;
        this.role = Roles.ADMIN;
    }

    public void enableTwoFactor() {
        this.isTwoFactorEnabled = true;
    }

    // TODO: registrar usuario normal desde admin
    /*
     * public User registerUser(RegisterUserCommand command) {
     * this.email = command.email();
     * this.password = command.password();
     * this.organizationName = command.organizationName();
     * this.isTwoFactorEnabled = false;
     * this.role = Roles.USER;
     * }
     */
}
