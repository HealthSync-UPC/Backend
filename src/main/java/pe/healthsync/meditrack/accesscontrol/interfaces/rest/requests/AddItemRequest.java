package pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests;

import pe.healthsync.meditrack.accesscontrol.domain.model.commands.AddItemCommand;

public record AddItemRequest(Long itemId) {
    public AddItemCommand toCommand(Long adminId, Long zoneId) {
        return new AddItemCommand(adminId, zoneId, itemId);
    }
}
