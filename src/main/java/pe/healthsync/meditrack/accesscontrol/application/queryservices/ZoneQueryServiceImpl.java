package pe.healthsync.meditrack.accesscontrol.application.queryservices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;
import pe.healthsync.meditrack.accesscontrol.domain.model.queries.GetAllZonesByAdminIdQuery;
import pe.healthsync.meditrack.accesscontrol.domain.services.ZoneQueryService;
import pe.healthsync.meditrack.accesscontrol.infrastructure.persistence.jpa.respositories.ZoneRepository;

@Service
public class ZoneQueryServiceImpl implements ZoneQueryService {
    @Autowired
    private ZoneRepository zoneRepository;

    @Override
    public List<Zone> handle(GetAllZonesByAdminIdQuery query) {
        return zoneRepository.findAllByAdminId(query.adminId());
    }

}
