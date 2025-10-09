package pe.healthsync.meditrack.iam.infrastructure.tokens.jwt;

import jakarta.servlet.http.HttpServletRequest;
import pe.healthsync.meditrack.iam.application.outboundservices.tokens.TokenService;

public interface BearerTokenService extends TokenService {
    String getBearerTokenFrom(HttpServletRequest token);
}