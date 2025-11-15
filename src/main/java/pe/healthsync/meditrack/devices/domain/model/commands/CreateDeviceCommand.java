package pe.healthsync.meditrack.devices.domain.model.commands;

public record CreateDeviceCommand(
                Long adminId,
                String name,
                String serialNumber,
                String type,
                String status,
                String unit) {

}
