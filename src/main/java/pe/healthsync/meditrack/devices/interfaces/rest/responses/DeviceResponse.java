package pe.healthsync.meditrack.devices.interfaces.rest.responses;

import java.util.List;

import pe.healthsync.meditrack.devices.domain.model.aggregates.Device;

public record DeviceResponse(
        Long id,
        String name,
        String serialNumber,
        String unit,
        String type,
        String location,
        String status,
        List<DeviceReadingResponse> readings) {
    public static DeviceResponse fromEntity(Device device) {
        var readings = device.getReadings().stream()
                .map(DeviceReadingResponse::fromEntity)
                .toList();

        return new DeviceResponse(
                device.getId(),
                device.getName(),
                device.getSerialNumber(),
                device.getUnit(),
                device.getType().name(),
                device.getLocation(),
                device.getStatus().name(),
                readings);
    }
}
