package pe.healthsync.meditrack.accesscontrol.domain.model.commands;

public record RemoveItemCommand(Long adminId, Long zoneId, Long itemId) {

}
