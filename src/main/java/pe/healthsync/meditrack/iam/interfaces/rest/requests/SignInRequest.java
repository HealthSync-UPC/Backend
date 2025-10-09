package pe.healthsync.meditrack.iam.interfaces.rest.requests;

import pe.healthsync.meditrack.iam.domain.model.commands.SignInCommand;

public record SignInRequest(String email, String password) {
    public SignInCommand toCommand() {
        return new SignInCommand(email, password);
    }
}
