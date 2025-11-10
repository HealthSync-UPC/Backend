package pe.healthsync.meditrack.accesscontrol.interfaces.rest.responses;

import pe.healthsync.meditrack.accesscontrol.domain.model.entities.AccessLog;

public record AccessLogResponse(Long id, String name, String accessTime, boolean accessGranted) {
    public static AccessLogResponse fromEntity(AccessLog accessLog) {
        var profile = accessLog.getUser().getProfile();
        return new AccessLogResponse(
                accessLog.getId(),
                profile != null ? profile.getFullName() : "Admin",
                accessLog.getAccessTime().toString(),
                accessLog.isAccessGranted());
    }
}
