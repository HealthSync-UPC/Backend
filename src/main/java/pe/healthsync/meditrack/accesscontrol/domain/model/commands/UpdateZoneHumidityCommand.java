package pe.healthsync.meditrack.accesscontrol.domain.model.commands;

public record UpdateZoneHumidityCommand(
        Long adminId,
        Long zoneId,
        Double minHumidity,
        Double maxHumidity) {

}
