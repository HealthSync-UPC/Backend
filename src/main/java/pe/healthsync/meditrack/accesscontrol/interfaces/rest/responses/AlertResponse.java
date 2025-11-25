package pe.healthsync.meditrack.accesscontrol.interfaces.rest.responses;

import java.time.Instant;

import pe.healthsync.meditrack.accesscontrol.domain.model.entities.Alert;
import pe.healthsync.meditrack.accesscontrol.domain.model.valueobjects.AlertType;

public record AlertResponse(
        Long id,
        AlertType type,
        Long zoneId,
        String location,
        Instant registeredAt) {

    public static AlertResponse fromEntity(Alert alert) {
        var zone = alert.getZone();
        var location = zone != null ? zone.getName() : null;
        var zoneId = zone != null ? zone.getId() : null;

        return new AlertResponse(
                alert.getId(),
                alert.getType(),
                zoneId,
                location,
                alert.getCreatedAt());
    }
}
