package pe.healthsync.meditrack.devices.application.commandservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.healthsync.meditrack.accesscontrol.infrastructure.persistence.jpa.respositories.ZoneRepository;
import pe.healthsync.meditrack.accesscontrol.interfaces.rest.responses.AlertResponse;
import pe.healthsync.meditrack.devices.domain.model.aggregates.Device;
import pe.healthsync.meditrack.devices.domain.model.commands.CreateDeviceCommand;
import pe.healthsync.meditrack.devices.domain.model.commands.CreateDeviceReadingCommand;
import pe.healthsync.meditrack.devices.domain.model.commands.DeleteDeviceCommand;
import pe.healthsync.meditrack.devices.domain.model.entities.DeviceReading;
import pe.healthsync.meditrack.devices.domain.model.valueobjects.DeviceType;
import pe.healthsync.meditrack.devices.domain.model.valueobjects.StatusType;
import pe.healthsync.meditrack.devices.domain.services.DeviceCommandService;
import pe.healthsync.meditrack.devices.infrastructure.persistence.jpa.respositories.DeviceRepository;
import pe.healthsync.meditrack.iam.domain.model.queries.GetUserByIdQuery;
import pe.healthsync.meditrack.iam.domain.services.UserQueryService;
import pe.healthsync.meditrack.shared.infrastructure.websocket.AppWebSocketHandler;
import pe.healthsync.meditrack.shared.infrastructure.websocket.WebSocketMessage;

@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private AppWebSocketHandler webSocketHandler;

    @Autowired
    private ObjectMapper objectMapper;

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

        var alert = device.addReading(deviceReading);

        if (alert != null) {
            zoneRepository.save(device.getZone());

            var zone = zoneRepository.findById(device.getZone().getId()).get();
            var alertEntity = zone.getAlerts().getLast();

            var email = zone.getAdmin().getEmail();
            var emailDomain = email.substring(email.indexOf('@') + 1);

            AlertResponse alertResponse = AlertResponse.fromEntity(alertEntity);

            WebSocketMessage wsMessage = new WebSocketMessage(
                    "alert",
                    alertResponse);

            try {
                String payloadJson = objectMapper.writeValueAsString(wsMessage);
                webSocketHandler.sendToDomain(emailDomain, payloadJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return deviceRepository.save(device);
    }

    @Override
    public void handle(DeleteDeviceCommand command) {
        var device = deviceRepository.findById(command.deviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found with id: " + command.deviceId()));

        if (!device.getAdmin().getId().equals(command.adminId())) {
            throw new IllegalArgumentException("This user does not have permission to delete this device.");
        }

        deviceRepository.delete(device);
    }

}
