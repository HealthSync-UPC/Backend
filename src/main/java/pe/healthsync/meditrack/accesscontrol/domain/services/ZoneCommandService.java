package pe.healthsync.meditrack.accesscontrol.domain.services;

import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.AddDeviceCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.AddMemberCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.AddItemCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.CreateZoneCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveDeviceCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveItemCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveMemberCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.TryAccessCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.UpdateZoneHumidityCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.UpdateZoneTemperatureCommand;

public interface ZoneCommandService {
    Zone handle(CreateZoneCommand command);

    Zone handle(AddMemberCommand command);

    Zone handle(RemoveMemberCommand command);

    Zone handle(TryAccessCommand command);

    Zone handle(AddDeviceCommand command);

    Zone handle(RemoveDeviceCommand command);

    Zone handle(AddItemCommand command);

    Zone handle(RemoveItemCommand command);

    Zone handle(UpdateZoneTemperatureCommand command);

    Zone handle(UpdateZoneHumidityCommand command);
}
