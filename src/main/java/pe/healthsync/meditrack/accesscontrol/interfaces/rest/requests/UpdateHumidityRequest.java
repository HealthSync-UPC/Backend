package pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests;

import pe.healthsync.meditrack.accesscontrol.domain.model.commands.UpdateZoneHumidityCommand;

public record UpdateHumidityRequest(Double minHumidity, Double maxHumidity) {
    public UpdateZoneHumidityCommand toCommand(Long adminId, Long zoneId) {
        return new UpdateZoneHumidityCommand(adminId, zoneId, minHumidity, maxHumidity);
    }
}
