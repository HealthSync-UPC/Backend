package pe.healthsync.meditrack.shared.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import pe.healthsync.meditrack.iam.domain.model.aggregates.User;
import pe.healthsync.meditrack.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;

@Component
public class AuthenticatedUserProvider {
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl userDetails)) {
            throw new IllegalStateException("No authenticated user found in context");
        }

        return userDetails.getUser();
    }
}
