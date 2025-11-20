package pe.healthsync.meditrack.accesscontrol.domain.services;

import java.util.List;

import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;
import pe.healthsync.meditrack.accesscontrol.domain.model.queries.GetAllZonesByAdminIdQuery;
import pe.healthsync.meditrack.accesscontrol.domain.model.queries.GetZoneByIdQuery;

public interface ZoneQueryService {
    Zone handle(GetZoneByIdQuery query);

    List<Zone> handle(GetAllZonesByAdminIdQuery query);
}
