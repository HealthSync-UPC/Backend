package pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests;

import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveMemberCommand;

public record RemoveMemberRequest(Long userId) {
    public RemoveMemberCommand toCommand(Long adminId, Long zoneId) {
        return new RemoveMemberCommand(adminId, zoneId, userId);
    }
}