package pe.healthsync.meditrack.devices.application.queryservices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.healthsync.meditrack.devices.domain.model.aggregates.Device;
import pe.healthsync.meditrack.devices.domain.model.queries.GetAllDevicesByAdminIdQuery;
import pe.healthsync.meditrack.devices.domain.services.DeviceQueryService;
import pe.healthsync.meditrack.devices.infrastructure.persistence.jpa.respositories.DeviceRepository;

@Service
public class DeviceQueryServiceImpl implements DeviceQueryService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public List<Device> handle(GetAllDevicesByAdminIdQuery query) {
        return deviceRepository.findAllByAdminId(query.adminId());
    }

}
