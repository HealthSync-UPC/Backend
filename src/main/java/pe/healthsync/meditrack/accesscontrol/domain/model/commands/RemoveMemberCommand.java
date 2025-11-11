package pe.healthsync.meditrack.accesscontrol.domain.model.commands;

public record RemoveMemberCommand(Long adminId, Long zoneId, Long userId) {

}
