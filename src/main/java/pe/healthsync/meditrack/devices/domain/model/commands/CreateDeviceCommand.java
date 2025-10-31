package pe.healthsync.meditrack.devices.domain.model.commands;

import pe.healthsync.meditrack.devices.domain.model.valueobjects.DeviceType;
import pe.healthsync.meditrack.devices.domain.model.valueobjects.StatusType;
import pe.healthsync.meditrack.iam.domain.model.aggregates.User;

public record CreateDeviceCommand(
                User admin,
                String name,
                String serialNumber,
                DeviceType type,
                String location,
                StatusType status,
                String unit) {

}
