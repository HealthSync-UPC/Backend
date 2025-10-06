package pe.healthsync.meditrack.iam.interfaces.rest.requests;

import pe.healthsync.meditrack.iam.domain.model.commands.Verify2FACommand;

public record VerifyTOTPRequest(String email, String code) {
    public Verify2FACommand toCommand() {
        return new Verify2FACommand(email, code);
    }
}
