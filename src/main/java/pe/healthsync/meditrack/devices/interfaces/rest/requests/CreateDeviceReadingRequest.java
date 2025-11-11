package pe.healthsync.meditrack.devices.interfaces.rest.requests;

import pe.healthsync.meditrack.devices.domain.model.commands.CreateDeviceReadingCommand;

public record CreateDeviceReadingRequest(
        Long deviceId,
        Double value) {
    public CreateDeviceReadingCommand toCommand() {
        return new CreateDeviceReadingCommand(
                deviceId,
                value);
    }
}
