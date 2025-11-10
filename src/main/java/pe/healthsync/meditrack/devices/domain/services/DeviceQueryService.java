package pe.healthsync.meditrack.devices.domain.services;

import java.util.List;

import pe.healthsync.meditrack.devices.domain.model.aggregates.Device;
import pe.healthsync.meditrack.devices.domain.model.queries.GetAllDevicesByAdminIdQuery;

public interface DeviceQueryService {
    List<Device> handle(GetAllDevicesByAdminIdQuery query);
}
