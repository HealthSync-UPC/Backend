package pe.healthsync.meditrack.devices.application.commandservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.healthsync.meditrack.devices.domain.model.aggregates.Device;
import pe.healthsync.meditrack.devices.domain.model.commands.CreateDeviceCommand;
import pe.healthsync.meditrack.devices.domain.model.commands.CreateDeviceReadingCommand;
import pe.healthsync.meditrack.devices.domain.model.entities.DeviceReading;
import pe.healthsync.meditrack.devices.domain.model.valueobjects.DeviceType;
import pe.healthsync.meditrack.devices.domain.model.valueobjects.StatusType;
import pe.healthsync.meditrack.devices.domain.services.DeviceCommandService;
import pe.healthsync.meditrack.devices.infrastructure.persistence.jpa.respositories.DeviceRepository;
import pe.healthsync.meditrack.iam.domain.model.queries.GetUserByIdQuery;
import pe.healthsync.meditrack.iam.domain.services.UserQueryService;

@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserQueryService userQueryService;

    @Override
    public Device handle(CreateDeviceCommand command) {
        DeviceType deviceType;
        try {
            deviceType = DeviceType.valueOf(command.type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid device type: " + command.type());
        }

        StatusType statusType;
        try {
            statusType = StatusType.valueOf(command.status().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status type: " + command.status());
        }

        var admin = userQueryService.handle(new GetUserByIdQuery(command.adminId()));

        var device = new Device(command, admin, deviceType, statusType);

        return deviceRepository.save(device);
    }

    @Override
    public Device handle(CreateDeviceReadingCommand command) {
        var device = deviceRepository.findById(command.deviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + command.deviceId()));

        var deviceReading = new DeviceReading(command);
        device.addReading(deviceReading);

        return deviceRepository.save(device);
    }

}
