package pe.healthsync.meditrack.devices.interfaces.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import pe.healthsync.meditrack.devices.domain.model.commands.DeleteDeviceCommand;
import pe.healthsync.meditrack.devices.domain.model.queries.GetAllDevicesByAdminIdQuery;
import pe.healthsync.meditrack.devices.domain.services.DeviceCommandService;
import pe.healthsync.meditrack.devices.domain.services.DeviceQueryService;
import pe.healthsync.meditrack.devices.interfaces.rest.requests.CreateDeviceReadingRequest;
import pe.healthsync.meditrack.devices.interfaces.rest.requests.CreateDeviceRequest;
import pe.healthsync.meditrack.devices.interfaces.rest.responses.DeviceResponse;
import pe.healthsync.meditrack.shared.infrastructure.security.AuthenticatedUserProvider;

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

    @Operation(summary = "Get all devices for the current user", description = "Returns a list of all devices associated with the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of devices retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponse.class))),
    })
    @GetMapping()
    public ResponseEntity<List<DeviceResponse>> getDevices() {
        var user = authenticatedUserProvider.getCurrentUser();
        var adminId = user.getRole().toString().equals("ADMIN") ? user.getId() : user.getAdmin().getId();

        var query = new GetAllDevicesByAdminIdQuery(adminId);
        var devices = deviceQueryService.handle(query);

        var devicesResponse = devices.stream()
                .map(DeviceResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(devicesResponse);
    }

    @Operation(summary = "Create a new device", description = "Creates a new device for the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponse.class))),
    })
    @PostMapping()
    public ResponseEntity<DeviceResponse> createDevice(@RequestBody CreateDeviceRequest request) {
        var adminId = authenticatedUserProvider.getCurrentUser().getId();
        var command = request.toCommand(adminId);

        var device = deviceCommandService.handle(command);

        var deviceResponse = DeviceResponse.fromEntity(device);

        return ResponseEntity.status(HttpStatus.CREATED).body(deviceResponse);
    }

    @Operation(summary = "Create a new device reading", description = "Creates a new reading for a device.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Device reading created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponse.class))),
    })
    @PostMapping("/readings")
    public ResponseEntity<DeviceResponse> createDeviceReading(@RequestBody CreateDeviceReadingRequest request) {
        var command = request.toCommand();

        var device = deviceCommandService.handle(command);

        var deviceResponse = DeviceResponse.fromEntity(device);

        return ResponseEntity.status(HttpStatus.CREATED).body(deviceResponse);
    }

    @Operation(summary = "Delete a device", description = "Deletes a device for the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Device deleted successfully"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        var adminId = authenticatedUserProvider.getCurrentUser().getId();
        var command = new DeleteDeviceCommand(adminId, id);

        deviceCommandService.handle(command);

        return ResponseEntity.noContent().build();
    }
}
