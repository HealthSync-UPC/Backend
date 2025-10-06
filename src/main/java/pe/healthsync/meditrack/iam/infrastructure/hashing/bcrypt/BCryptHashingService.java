package pe.healthsync.meditrack.iam.infrastructure.hashing.bcrypt;

import org.springframework.security.crypto.password.PasswordEncoder;

import pe.healthsync.meditrack.iam.application.outboundservices.hashing.HashingService;

public interface BCryptHashingService extends HashingService, PasswordEncoder {

}
