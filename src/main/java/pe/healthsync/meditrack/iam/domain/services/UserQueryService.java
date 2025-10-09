package pe.healthsync.meditrack.iam.domain.services;

import pe.healthsync.meditrack.iam.domain.model.aggregates.User;
import pe.healthsync.meditrack.iam.domain.model.queries.GetUserByIdQuery;

public interface UserQueryService {
    User handle(GetUserByIdQuery query);
}
