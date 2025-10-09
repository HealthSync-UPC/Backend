package pe.healthsync.meditrack.profiles.interfaces.rest.responses;

import pe.healthsync.meditrack.profiles.domain.model.aggregates.Profile;

public record ProfileResource(
        String firstName,
        String lastName,
        String position,
        String email,
        String qr) {

    public static ProfileResource fromEntity(Profile profile, String email, String qr) {
        return new ProfileResource(
                profile.getFirstName(),
                profile.getLastName(),
                profile.getPosition(),
                email,
                qr);
    }
}
