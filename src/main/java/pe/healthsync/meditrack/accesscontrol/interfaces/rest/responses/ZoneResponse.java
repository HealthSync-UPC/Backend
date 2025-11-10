package pe.healthsync.meditrack.accesscontrol.interfaces.rest.responses;

import java.util.List;

import pe.healthsync.meditrack.accesscontrol.domain.model.aggregates.Zone;

public record ZoneResponse(Long id, String name, List<MemberResponse> members, List<AccessLogResponse> accessLogs) {
    public static ZoneResponse fromEntity(Zone zone) {
        List<MemberResponse> memberResponses = zone.getMembers().stream()
                .map(MemberResponse::fromEntity)
                .toList();

        List<AccessLogResponse> accessLogResponses = zone.getAccessLogs().stream()
                .map(AccessLogResponse::fromEntity)
                .toList();

        return new ZoneResponse(
                zone.getId(),
                zone.getName(),
                memberResponses,
                accessLogResponses);
    }
}
