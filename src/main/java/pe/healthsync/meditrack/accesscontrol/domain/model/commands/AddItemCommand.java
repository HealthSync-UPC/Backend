package pe.healthsync.meditrack.accesscontrol.domain.model.commands;

public record AddItemCommand(Long adminId, Long zoneId, Long itemId) {

}
