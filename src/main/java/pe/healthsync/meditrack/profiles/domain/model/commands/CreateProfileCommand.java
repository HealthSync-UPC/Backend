package pe.healthsync.meditrack.profiles.domain.model.commands;

import pe.healthsync.meditrack.iam.domain.model.aggregates.User;

public record CreateProfileCommand(
        Long adminId,
        String firstName,
        String lastName,
        String position,
        User admin,
        User user,
        String email,
        String password) {

}
