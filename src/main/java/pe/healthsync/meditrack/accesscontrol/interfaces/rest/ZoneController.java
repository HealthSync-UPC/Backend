package pe.healthsync.meditrack.accesscontrol.interfaces.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveDeviceCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveItemCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.commands.RemoveMemberCommand;
import pe.healthsync.meditrack.accesscontrol.domain.model.queries.GetAllZonesByAdminIdQuery;
import pe.healthsync.meditrack.accesscontrol.domain.model.queries.GetZoneByIdQuery;
import pe.healthsync.meditrack.accesscontrol.domain.services.ZoneCommandService;
import pe.healthsync.meditrack.accesscontrol.domain.services.ZoneQueryService;
import pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests.AccesRequest;
import pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests.AddDeviceRequest;
import pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests.AddItemRequest;
import pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests.AddMemberRequest;
import pe.healthsync.meditrack.accesscontrol.interfaces.rest.requests.CreateZoneRequest;
import pe.healthsync.meditrack.accesscontrol.interfaces.rest.responses.ZoneResponse;
import pe.healthsync.meditrack.shared.infrastructure.security.AuthenticatedUserProvider;

@RestController
@RequestMapping(value = "/api/v1/zones", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Zones", description = "Zones Endpoints")
public class ZoneController {

        @Autowired
        private ZoneCommandService zoneCommandService;

        @Autowired
        private ZoneQueryService zoneQueryService;

        @Autowired
        private AuthenticatedUserProvider authenticatedUserProvider;

        @Operation(summary = "Get zone by ID", description = "Returns a zone belonging to the currently authenticated user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Zone retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZoneResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Zone not found"),
        })
        @GetMapping("/{id}")
        public ResponseEntity<ZoneResponse> getZoneById(@PathVariable Long id) {
                var adminId = authenticatedUserProvider.getCurrentUser().getId();
                var query = new GetZoneByIdQuery(adminId, id);
                var zone = zoneQueryService.handle(query);

                var response = ZoneResponse.fromEntity(zone);

                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Get all zones for the current user", description = "Returns a list of all zones belonging to the currently authenticated user.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Zones retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZoneResponse.class)))
        })
        @GetMapping()
        public ResponseEntity<List<ZoneResponse>> getZones() {
                var adminId = authenticatedUserProvider.getCurrentUser().getId();
                var query = new GetAllZonesByAdminIdQuery(adminId);
                var zones = zoneQueryService.handle(query);

                var responses = zones.stream()
                                .map(ZoneResponse::fromEntity)
                                .toList();

                return ResponseEntity.ok(responses);
        }

        @Operation(summary = "Create a new zone", description = "Creates a new zone for the currently authenticated user.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Zone created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZoneResponse.class)))
        })
        @PostMapping()
        public ResponseEntity<ZoneResponse> createZone(@RequestBody CreateZoneRequest request) {
                var adminId = authenticatedUserProvider.getCurrentUser().getId();
                var command = request.toCommand(adminId);

                var zone = zoneCommandService.handle(command);

                return ResponseEntity.status(HttpStatus.CREATED).body(ZoneResponse.fromEntity(zone));
        }

        @Operation(summary = "Add a member to a zone", description = "Adds a member to the specified zone. Only the zone admin can perform this action.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Member added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZoneResponse.class)))
        })
        @PostMapping("/{id}/members")
        public ResponseEntity<ZoneResponse> addMember(@PathVariable("id") Long zoneId,
                        @RequestBody AddMemberRequest request) {
                var adminId = authenticatedUserProvider.getCurrentUser().getId();
                var command = request.toCommand(adminId, zoneId);

                var zone = zoneCommandService.handle(command);

                return ResponseEntity.ok(ZoneResponse.fromEntity(zone));
        }

        @Operation(summary = "Remove a member from a zone", description = "Removes a member from the specified zone. Only the zone admin can perform this action.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Member removed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZoneResponse.class)))
        })
        @DeleteMapping("/{id}/members/{memberId}")
        public ResponseEntity<ZoneResponse> removeMember(@PathVariable("id") Long zoneId,
                        @PathVariable("memberId") Long memberId) {
                var adminId = authenticatedUserProvider.getCurrentUser().getId();
                var command = new RemoveMemberCommand(adminId, zoneId, memberId);

                var zone = zoneCommandService.handle(command);

                return ResponseEntity.ok(ZoneResponse.fromEntity(zone));
        }

        @Operation(summary = "Try access to a zone", description = "Attempts to access the specified zone with the provided access data. Returns the zone if access is granted.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Access granted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZoneResponse.class))),
                        @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZoneResponse.class)))
        })
        @PostMapping("/{id}/access")
        public ResponseEntity<ZoneResponse> tryAccess(@PathVariable("id") Long zoneId,
                        @RequestBody AccesRequest request) {
                var command = request.toCommand(zoneId);
                var zone = zoneCommandService.handle(command);
                var response = ZoneResponse.fromEntity(zone);

                var isAccessGranted = zone.getAccessLogs().getLast().isAccessGranted();

                if (isAccessGranted == Boolean.TRUE) {
                        return ResponseEntity.ok(response);
                }

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        @Operation(summary = "Add a device to a zone", description = "Adds a device to the specified zone. Only the zone admin can perform this action.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Device added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZoneResponse.class)))
        })
        @PostMapping("/{id}/devices")
        public ResponseEntity<ZoneResponse> addDevice(@PathVariable("id") Long zoneId,
                        @RequestBody AddDeviceRequest request) {
                var adminId = authenticatedUserProvider.getCurrentUser().getId();
                var command = request.toCommand(adminId, zoneId);

                var zone = zoneCommandService.handle(command);

                return ResponseEntity.ok(ZoneResponse.fromEntity(zone));
        }

        @Operation(summary = "Remove a device from a zone", description = "Removes a device from the specified zone. Only the zone admin can perform this action.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Device removed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZoneResponse.class)))
        })
        @DeleteMapping("/{id}/devices/{deviceId}")
        public ResponseEntity<ZoneResponse> removeDevice(@PathVariable("id") Long zoneId,
                        @PathVariable("deviceId") Long deviceId) {
                var adminId = authenticatedUserProvider.getCurrentUser().getId();
                var command = new RemoveDeviceCommand(adminId, zoneId, deviceId);
                var zone = zoneCommandService.handle(command);

                return ResponseEntity.ok(ZoneResponse.fromEntity(zone));
        }

        @Operation(summary = "Add a item to a zone", description = "Adds a item to the specified zone. Only the zone admin can perform this action.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Item added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZoneResponse.class)))
        })
        @PostMapping("/{id}/items")
        public ResponseEntity<ZoneResponse> addItem(@PathVariable("id") Long zoneId,
                        @RequestBody AddItemRequest request) {
                var adminId = authenticatedUserProvider.getCurrentUser().getId();
                var command = request.toCommand(adminId, zoneId);

                var zone = zoneCommandService.handle(command);

                return ResponseEntity.ok(ZoneResponse.fromEntity(zone));
        }

        @Operation(summary = "Remove a item from a zone", description = "Removes a item from the specified zone. Only the zone admin can perform this action.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Item removed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ZoneResponse.class)))
        })
        @DeleteMapping("/{id}/items/{itemId}")
        public ResponseEntity<ZoneResponse> removeItem(@PathVariable("id") Long zoneId,
                        @PathVariable("itemId") Long itemId) {
                var adminId = authenticatedUserProvider.getCurrentUser().getId();
                var command = new RemoveItemCommand(adminId, zoneId, itemId);
                var zone = zoneCommandService.handle(command);

                return ResponseEntity.ok(ZoneResponse.fromEntity(zone));
        }

}
