package pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests;

import pe.healthsync.meditrack.accesscontrol.domain.model.commands.AddMemberCommand;

public record AddMemberRequest(Long userId) {
    public AddMemberCommand toCommand(Long adminId, Long zoneId) {
        return new AddMemberCommand(adminId, zoneId, userId);
    }
}
