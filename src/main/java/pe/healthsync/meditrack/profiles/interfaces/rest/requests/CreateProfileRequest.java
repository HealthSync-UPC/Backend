package pe.healthsync.meditrack.profiles.interfaces.rest.requests;

import pe.healthsync.meditrack.profiles.domain.model.commands.CreateProfileCommand;

public record CreateProfileRequest(
                String email,
                String password,
                String firstName,
                String lastName,
                String position) {

        public CreateProfileCommand toCommand(Long adminId) {
                return new CreateProfileCommand(
                                firstName,
                                lastName,
                                position,
                                adminId,
                                email,
                                password);
        }
}