package pe.healthsync.meditrack.profiles.interfaces.rest.responses;

import pe.healthsync.meditrack.profiles.domain.model.aggregates.Profile;

public record ProfileResponse(
        String firstName,
        String lastName,
        String position,
        String email,
        String qr) {

    public static ProfileResponse fromEntity(Profile profile, String email, String qr) {
        return new ProfileResponse(
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPosition(),
                email,
                qr);
    }
}
