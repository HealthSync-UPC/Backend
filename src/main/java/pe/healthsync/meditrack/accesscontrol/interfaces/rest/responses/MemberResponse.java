package pe.healthsync.meditrack.accesscontrol.interfaces.rest.responses;

import pe.healthsync.meditrack.iam.domain.model.aggregates.User;

public record MemberResponse(Long id, String name) {
    public static MemberResponse fromEntity(User user) {
        return new MemberResponse(user.getId(), user.getProfile().getFullName());
    }
}
