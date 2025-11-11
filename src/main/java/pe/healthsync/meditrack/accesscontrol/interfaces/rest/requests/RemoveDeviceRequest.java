package pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests;

import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveDeviceCommand;

public record RemoveDeviceRequest(Long deviceId) {
    public RemoveDeviceCommand toCommand(Long adminId, Long zoneId) {
        return new RemoveDeviceCommand(adminId, zoneId, deviceId);
    }
}