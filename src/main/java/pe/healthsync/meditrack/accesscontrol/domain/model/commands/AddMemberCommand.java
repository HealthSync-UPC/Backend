package pe.healthsync.meditrack.accesscontrol.domain.model.commands;

public record AddMemberCommand(Long adminId, Long zoneId, Long userId) {

}
