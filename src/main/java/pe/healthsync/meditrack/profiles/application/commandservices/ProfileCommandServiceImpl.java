package pe.healthsync.meditrack.profiles.application.commandservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.vavr.Tuple3;
import pe.healthsync.meditrack.iam.domain.model.commands.GenerateUserQrCommand;
import pe.healthsync.meditrack.iam.domain.model.commands.RegisterUserCommand;
import pe.healthsync.meditrack.iam.domain.services.UserCommandService;
import pe.healthsync.meditrack.profiles.domain.model.aggregates.Profile;
import pe.healthsync.meditrack.profiles.domain.model.commands.CreateProfileCommand;
import pe.healthsync.meditrack.profiles.domain.services.ProfileCommandService;
import pe.healthsync.meditrack.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserCommandService userCommandService;

    @Override
    public Tuple3<Profile, String, String> handle(CreateProfileCommand command) {
        var createUserCommand = new RegisterUserCommand(
                command.adminId(),
                command.email(),
                command.password(),
                null);

        var createdUser = userCommandService.handle(createUserCommand);

        var createProfileCommand = new CreateProfileCommand(
                command.firstName(),
                command.lastName(),
                command.position(),
                command.adminId(),
                createdUser.getId(),
                command.email(),
                command.password());

        Profile profile = new Profile(createProfileCommand);

        var createdProfile = profileRepository.save(profile);

        var generateQrCommand = new GenerateUserQrCommand(createdUser.getEmail(), command.password());

        var qr = userCommandService.handle(generateQrCommand);

        return new Tuple3<>(createdProfile, createdUser.getEmail(), qr);
    }
}
