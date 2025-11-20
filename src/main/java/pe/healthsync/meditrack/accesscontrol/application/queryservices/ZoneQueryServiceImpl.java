package pe.healthsync.meditrack.accesscontrol.application.queryservices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;
import pe.healthsync.meditrack.accesscontrol.domain.model.queries.GetAllZonesByAdminIdQuery;
import pe.healthsync.meditrack.accesscontrol.domain.model.queries.GetZoneByIdQuery;
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

    @Override
    public Zone handle(GetZoneByIdQuery query) {
        var zone = zoneRepository.findById(query.zoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        if (!zone.getAdmin().getId().equals(query.adminId())) {
            throw new SecurityException("Unauthorized access to zone");
        }

        return zone;
    }

}
