package pe.healthsync.meditrack.iam.interfaces.rest.requests;

import pe.healthsync.meditrack.iam.domain.model.commands.SignUpCommand;

public record SignUpRequest(
        String email,
        String password,
        String organizationName) {

    public SignUpCommand toCommand() {
        return new SignUpCommand(email, password, organizationName);
    }
}
