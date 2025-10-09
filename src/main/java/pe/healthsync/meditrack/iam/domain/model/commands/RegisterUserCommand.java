package pe.healthsync.meditrack.iam.domain.model.commands;

public record RegisterUserCommand(
                Long adminId,
                String email,
                String password,
                String twoFactorSecret) {

}
