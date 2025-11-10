package pe.healthsync.meditrack.devices.domain.model.commands;

public record CreateDeviceReadingCommand(Long deviceId, Double value) {

}
