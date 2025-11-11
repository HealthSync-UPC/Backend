package pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests;

import pe.healthsync.meditrack.accesscontrol.domain.model.commands.TryAccessCommand;

public record AccesRequest(Long userId) {
    public TryAccessCommand toCommand(Long zoneId) {
        return new TryAccessCommand(zoneId, userId);
    }
}
