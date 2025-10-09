package pe.healthsync.meditrack.profiles.domain.services;

import io.vavr.Tuple3;
import pe.healthsync.meditrack.profiles.domain.model.aggregates.Profile;
import pe.healthsync.meditrack.profiles.domain.model.commands.CreateProfileCommand;

public interface ProfileCommandService {
    Tuple3<Profile, String, String> handle(CreateProfileCommand command);
}
