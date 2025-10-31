package pe.healthsync.meditrack.devices.interfaces.rest.responses;

import java.time.Instant;

import pe.healthsync.meditrack.devices.domain.model.entities.DeviceReading;

public record DeviceReadingResponse(
        Long id,
        String value,
        Double numericValue,
        Instant readingAt) {
    public static DeviceReadingResponse fromEntity(DeviceReading reading) {
        return new DeviceReadingResponse(
                reading.getId(),
                reading.getValue(),
                reading.getNumericValue(),
                reading.getReadingAt());
    }
}
