package pe.healthsync.meditrack.profiles.application.commandservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.vavr.Tuple3;
import pe.healthsync.meditrack.iam.domain.model.commands.GenerateUserQrCommand;
import pe.healthsync.meditrack.iam.domain.model.commands.RegisterUserCommand;
import pe.healthsync.meditrack.iam.domain.model.queries.GetUserByIdQuery;
import pe.healthsync.meditrack.iam.domain.services.UserCommandService;
import pe.healthsync.meditrack.iam.domain.services.UserQueryService;
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

        @Autowired
        private UserQueryService userQueryService;

        @Override
        public Tuple3<Profile, String, String> handle(CreateProfileCommand command) {
                var admin = userQueryService.handle(new GetUserByIdQuery(command.adminId()));

                var createUserCommand = new RegisterUserCommand(
                                admin.getId(),
                                command.email(),
                                command.password(),
                                null);

                var createdUser = userCommandService.handle(createUserCommand);

                Profile profile = new Profile(command, admin, createdUser);

                var createdProfile = profileRepository.save(profile);

                var generateQrCommand = new GenerateUserQrCommand(createdUser.getEmail(), command.password());

                var qr = userCommandService.handle(generateQrCommand);

                return new Tuple3<>(createdProfile, createdUser.getEmail(), qr);
        }
}
