package pe.healthsync.meditrack.accesscontrol.interfaces.rest.responses;

import pe.healthsync.meditrack.accesscontrol.domain.model.entities.AccessLog;

public record AccessLogResponse(Long id, String name, String accessTime, boolean accessGranted) {
    public static AccessLogResponse fromEntity(AccessLog accessLog) {
        return new AccessLogResponse(
                accessLog.getId(),
                accessLog.getUser().getProfile().getFullName(),
                accessLog.getAccessTime().toString(),
                accessLog.isAccessGranted());
    }
}
