package pe.healthsync.meditrack.iam.application.outboundservices.hashing;

public interface HashingService {
    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}