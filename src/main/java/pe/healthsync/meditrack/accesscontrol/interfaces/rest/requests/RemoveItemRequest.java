package pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests;

import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveItemCommand;

public record RemoveItemRequest(Long itemId) {
    public RemoveItemCommand toCommand(Long adminId, Long zoneId) {
        return new RemoveItemCommand(adminId, zoneId, itemId);
    }
}
