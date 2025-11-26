package pe.healthsync.meditrack.accesscontrol.domain.model.commands;

public record UpdateZoneTemperatureCommand(
        Long adminId,
        Long zoneId,
        Double minTemperature,
        Double maxTemperature) {

}
