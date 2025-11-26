package pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests;

import pe.healthsync.meditrack.accesscontrol.domain.model.commands.UpdateZoneTemperatureCommand;

public record UpdateTemperatureRequest(Double minTemperature, Double maxTemperature) {
    public UpdateZoneTemperatureCommand toCommand(Long adminId, Long zoneId) {
        return new UpdateZoneTemperatureCommand(adminId, zoneId, minTemperature, maxTemperature);
    }
}
