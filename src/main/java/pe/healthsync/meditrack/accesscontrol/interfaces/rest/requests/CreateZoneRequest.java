package pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests;

import java.util.List;

import pe.healthsync.meditrack.accesscontrol.domain.model.commands.CreateZoneCommand;

public record CreateZoneRequest(
        String name,
        Long deviceId,
        List<Long> deviceIds,
        List<Long> itemIds,
        List<Long> memberIds) {
    public CreateZoneCommand toCommand(Long adminId) {
        return new CreateZoneCommand(
                adminId,
                name,
                deviceId,
                deviceIds,
                itemIds,
                memberIds);
    }
}
