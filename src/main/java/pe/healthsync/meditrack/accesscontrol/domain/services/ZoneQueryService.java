package pe.healthsync.meditrack.accesscontrol.domain.services;

import java.util.List;

import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;
import pe.healthsync.meditrack.accesscontrol.domain.model.queries.GetAllZonesByAdminIdQuery;

public interface ZoneQueryService {
    List<Zone> handle(GetAllZonesByAdminIdQuery query);
}
