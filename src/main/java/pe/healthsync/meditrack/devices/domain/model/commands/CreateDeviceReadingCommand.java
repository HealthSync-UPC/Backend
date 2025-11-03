package pe.healthsync.meditrack.devices.domain.model.commands;

public record CreateDeviceReadingCommand(Long deviceId, String value, Double numericValue) {

}
