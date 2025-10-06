package pe.healthsync.meditrack.iam.domain.services;

import pe.healthsync.meditrack.iam.domain.model.aggregates.User;
import pe.healthsync.meditrack.iam.domain.model.commands.GenerateUserQrCommand;
import pe.healthsync.meditrack.iam.domain.model.commands.SignInCommand;
import pe.healthsync.meditrack.iam.domain.model.commands.SignUpCommand;
import pe.healthsync.meditrack.iam.domain.model.commands.Verify2FACommand;

public interface UserCommandService {
    User handle(SignUpCommand command);

    User handle(SignInCommand command);

    String handle(Verify2FACommand command);

    String handle(GenerateUserQrCommand command);
}
