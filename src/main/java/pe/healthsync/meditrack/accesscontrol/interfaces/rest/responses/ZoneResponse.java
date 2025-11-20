package pe.healthsync.meditrack.accesscontrol.interfaces.rest.responses;

import java.util.List;

import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;
import pe.healthsync.meditrack.devices.domain.model.valueobjects.DeviceType;
import pe.healthsync.meditrack.devices.interfaces.rest.responses.DeviceResponse;
import pe.healthsync.meditrack.inventory.interfaces.rest.responses.ItemResponse;

public record ZoneResponse(
                Long id,
                String name,
                DeviceResponse nfcDevice,
                List<DeviceResponse> devices,
                List<ItemResponse> items,
                List<MemberResponse> members,
                List<AccessLogResponse> accessLogs) {
        public static ZoneResponse fromEntity(Zone zone) {
                List<MemberResponse> memberResponses = zone.getMembers().stream()
                                .map(MemberResponse::fromEntity)
                                .toList();

                DeviceResponse nfDeviceResponse = DeviceResponse.fromEntity(zone.getNfcDevice());

                List<DeviceResponse> deviceResponses = zone.getDevices().stream()
                                .filter(d -> d.getType() != DeviceType.ACCESS_NFC)
                                .map(DeviceResponse::fromEntity)
                                .toList();

                List<ItemResponse> itemResponses = zone.getItems().stream()
                                .map(ItemResponse::fromEntity)
                                .toList();

                List<AccessLogResponse> accessLogResponses = zone.getAccessLogs().stream()
                                .map(AccessLogResponse::fromEntity)
                                .toList();

                return new ZoneResponse(
                                zone.getId(),
                                zone.getName(),
                                nfDeviceResponse,
                                deviceResponses,
                                itemResponses,
                                memberResponses,
                                accessLogResponses);
        }
}
