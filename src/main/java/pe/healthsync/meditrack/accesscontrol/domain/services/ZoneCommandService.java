package pe.healthsync.meditrack.accesscontrol.domain.services;

import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.AddMemberCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.CreateZoneCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveMemberCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.TryAccessCommand;

public interface ZoneCommandService {
    Zone handle(CreateZoneCommand command);

    Zone handle(AddMemberCommand command);

    Zone handle(RemoveMemberCommand command);

    Zone handle(TryAccessCommand command);
}
