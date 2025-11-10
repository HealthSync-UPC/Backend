package pe.healthsync.meditrack.profiles.domain.services;

import java.util.List;

import io.vavr.Tuple3;
import pe.healthsync.meditrack.profiles.domain.model.aggregates.Profile;
import pe.healthsync.meditrack.profiles.domain.model.queries.GetProfileByUserIdQuery;
import pe.healthsync.meditrack.profiles.domain.model.queries.GetProfilesByAdminIQuery;

public interface ProfileQueryService {
    Tuple3<Profile, String, String> handle(GetProfileByUserIdQuery query);

    List<Tuple3<Profile, String, String>> handle(GetProfilesByAdminIQuery query);
}
