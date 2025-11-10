package pe.healthsync.meditrack.accesscontrol.domain.model.commands;

import java.util.List;

public record CreateZoneCommand(Long adminId, String name, Long deviceId, List<Long> memberIds) {

}
