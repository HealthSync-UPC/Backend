package pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests;

import pe.healthsync.meditrack.accesscontrol.domain.model.commands.AddDeviceCommand;

public record AddDeviceRequest(Long deviceId) {
    public AddDeviceCommand toCommand(Long adminId, Long zoneId) {
        return new AddDeviceCommand(adminId, zoneId, deviceId);
    }
}
