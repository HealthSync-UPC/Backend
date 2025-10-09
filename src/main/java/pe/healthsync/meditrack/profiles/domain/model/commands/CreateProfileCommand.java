package pe.healthsync.meditrack.profiles.domain.model.commands;

public record CreateProfileCommand(
                String firstName,
                String lastName,
                String position,
                Long adminId,
                String email,
                String password) {

}
