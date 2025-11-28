package pe.healthsync.meditrack.iam.application.outboundservices.tokens;

public interface TokenService {
    String generateToken(String email, String role, Long id);

    String getEmailFromToken(String token);

    boolean validateToken(String token);
}
