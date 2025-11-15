package pe.healthsync.meditrack.devices.interfaces.rest.requests;

import pe.healthsync.meditrack.devices.domain.model.commands.CreateDeviceCommand;

public record CreateDeviceRequest(
        String name,
        String serialNumber,
        String type,
        Long locationId,
        String status,
        String unit) {
    public CreateDeviceCommand toCommand(Long adminId) {
        return new CreateDeviceCommand(
                adminId,
                name,
                serialNumber,
                type,
                locationId,
                status,
                unit);
    }
}
