package pe.healthsync.meditrack.accesscontrol.domain.model.commands;

public record AddDeviceCommand(Long adminId, Long zoneId, Long deviceId) {

}
