package pe.healthsync.meditrack.profiles.domain.services;

import io.vavr.Tuple3;
import pe.healthsync.meditrack.profiles.domain.model.aggregates.Profile;
import pe.healthsync.meditrack.profiles.domain.model.queries.GetProfileByUserId;

public interface ProfileQueryService {
    Tuple3<Profile, String, String> handle(GetProfileByUserId query);
}
