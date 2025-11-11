package pe.healthsync.meditrack.devices.domain.services;

import pe.healthsync.meditrack.devices.domain.model.aggregates.Device;
import pe.healthsync.meditrack.devices.domain.model.commands.CreateDeviceCommand;
import pe.healthsync.meditrack.devices.domain.model.commands.CreateDeviceReadingCommand;
import pe.healthsync.meditrack.devices.domain.model.commands.DeleteDeviceCommand;

public interface DeviceCommandService {
    Device handle(CreateDeviceCommand command);

    Device handle(CreateDeviceReadingCommand command);

    void handle(DeleteDeviceCommand command);
}
