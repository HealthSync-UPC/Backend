package pe.healthsync.meditrack.devices.interfaces.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import pe.healthsync.meditrack.devices.domain.model.queries.GetAllDevicesByAdminIdQuery;
import pe.healthsync.meditrack.devices.domain.services.DeviceCommandService;
import pe.healthsync.meditrack.devices.domain.services.DeviceQueryService;
import pe.healthsync.meditrack.devices.interfaces.rest.requests.CreateDeviceReadingRequest;
import pe.healthsync.meditrack.devices.interfaces.rest.requests.CreateDeviceRequest;
import pe.healthsync.meditrack.devices.interfaces.rest.responses.DeviceResponse;
import pe.healthsync.meditrack.shared.infrastructure.security.AuthenticatedUserProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/api/v1/devices", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Devices", description = "Devices Endpoints")
public class DeviceController {
    @Autowired
    private DeviceCommandService deviceCommandService;

    @Autowired
    private DeviceQueryService deviceQueryService;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @GetMapping()
    public ResponseEntity<List<DeviceResponse>> getDevices() {
        var adminId = authenticatedUserProvider.getCurrentUser().getId();
        var query = new GetAllDevicesByAdminIdQuery(adminId);
        var devices = deviceQueryService.handle(query);

        var devicesResponse = devices.stream()
                .map(DeviceResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(devicesResponse);
    }

    @PostMapping()
    public ResponseEntity<DeviceResponse> createDevice(@RequestBody CreateDeviceRequest request) {
        var adminId = authenticatedUserProvider.getCurrentUser().getId();
        var command = request.toCommand(adminId);

        var device = deviceCommandService.handle(command);

        var deviceResponse = DeviceResponse.fromEntity(device);

        return ResponseEntity.ok(deviceResponse);
    }

    @PostMapping("/readings")
    public ResponseEntity<DeviceResponse> createDeviceReading(@RequestBody CreateDeviceReadingRequest request) {
        var command = request.toCommand();

        var device = deviceCommandService.handle(command);

        var deviceResponse = DeviceResponse.fromEntity(device);

        return ResponseEntity.ok(deviceResponse);
    }
}
