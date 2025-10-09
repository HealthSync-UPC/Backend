package pe.healthsync.meditrack.profiles.application.queryservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.vavr.Tuple3;
import pe.healthsync.meditrack.iam.domain.model.queries.GetUserByIdQuery;
import pe.healthsync.meditrack.iam.domain.services.UserQueryService;
import pe.healthsync.meditrack.iam.infrastructure.totp.TOTPService;
import pe.healthsync.meditrack.profiles.domain.model.aggregates.Profile;
import pe.healthsync.meditrack.profiles.domain.model.queries.GetProfileByUserId;
import pe.healthsync.meditrack.profiles.domain.services.ProfileQueryService;
import pe.healthsync.meditrack.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private TOTPService totpService;

    @Override
    public Tuple3<Profile, String, String> handle(GetProfileByUserId query) {
        var profile = profileRepository.findByUserId(query.userId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        var userQuery = new GetUserByIdQuery(query.userId());

        var user = userQueryService.handle(userQuery);

        try {
            var qr = totpService.generateQrCodeImage(user.getOrganizationName(), user.getEmail(),
                    user.getTwoFactorSecret());

            return new Tuple3<>(profile, user.getEmail(), qr);
        } catch (Exception e) {
            throw new RuntimeException("Error generating QR", e);
        }

    }
}
