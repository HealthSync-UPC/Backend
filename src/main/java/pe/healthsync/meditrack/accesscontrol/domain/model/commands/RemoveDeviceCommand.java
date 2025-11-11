package pe.healthsync.meditrack.accesscontrol.domain.model.commands;

public record RemoveDeviceCommand(Long adminId, Long zoneId, Long deviceId) {

}
