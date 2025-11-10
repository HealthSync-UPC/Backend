package pe.healthsync.meditrack.profiles.domain.model.commands;

public record CreateProfileCommand(
                Long adminId,
                String firstName,
                String lastName,
                String position,
                String email,
                String password) {

}
