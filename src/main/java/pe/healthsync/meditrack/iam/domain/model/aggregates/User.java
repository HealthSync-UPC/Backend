package pe.healthsync.meditrack.iam.domain.model.aggregates;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.healthsync.meditrack.iam.domain.model.commands.RegisterUserCommand;
import pe.healthsync.meditrack.iam.domain.model.commands.SignUpCommand;
import pe.healthsync.meditrack.iam.domain.model.valueobjects.Roles;
import pe.healthsync.meditrack.profiles.domain.model.aggregates.Profile;
import pe.healthsync.meditrack.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AuditableAbstractAggregateRoot<User> {
    private String email;

    private String password;

    private String organizationName;

    private String twoFactorSecret;

    private boolean isTwoFactorEnabled;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private User admin;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, optional = true)
    @Setter
    private Profile profile;

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

    public User registerUser(RegisterUserCommand command) {
        var domainEmail = this.email.split("@")[1];
        var localPart = command.email().split("@")[0];
        var email = localPart + "@" + domainEmail;
        var organizationName = this.organizationName;

        var newUser = new User(
                email,
                command.password(),
                organizationName,
                command.twoFactorSecret(),
                false,
                Roles.USER,
                this,
                null);

        return newUser;
    }

}
