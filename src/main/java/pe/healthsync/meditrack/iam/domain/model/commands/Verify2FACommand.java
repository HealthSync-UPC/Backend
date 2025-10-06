package pe.healthsync.meditrack.iam.domain.model.commands;

public record Verify2FACommand(String email, String code) {

}
