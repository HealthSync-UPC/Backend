package pe.healthsync.meditrack.iam.application.commandservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.healthsync.meditrack.iam.application.outboundservices.hashing.HashingService;
import pe.healthsync.meditrack.iam.application.outboundservices.tokens.TokenService;
import pe.healthsync.meditrack.iam.domain.model.aggregates.User;
import pe.healthsync.meditrack.iam.domain.model.commands.GenerateUserQrCommand;
import pe.healthsync.meditrack.iam.domain.model.commands.SignInCommand;
import pe.healthsync.meditrack.iam.domain.model.commands.SignUpCommand;
import pe.healthsync.meditrack.iam.domain.model.commands.Verify2FACommand;
import pe.healthsync.meditrack.iam.domain.services.UserCommandService;
import pe.healthsync.meditrack.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.healthsync.meditrack.iam.infrastructure.totp.TOTPService;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashingService hashingService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TOTPService totpService;

    @Override
    public User handle(SignUpCommand command) {
        if (userRepository.existsByEmail(command.email()))
            throw new RuntimeException("Email already exists");

        String hashedPassword = hashingService.encode(command.password());

        var hashedCommand = new SignUpCommand(command.email(), hashedPassword, command.organizationName());

        var secret = totpService.generateSecret();

        var user = new User(hashedCommand, secret);

        return userRepository.save(user);
    }

    @Override
    public User handle(SignInCommand command) {
        var userOpt = userRepository.findByEmail(command.email());
        if (userOpt.isEmpty())
            throw new IllegalArgumentException("Invalid credentials");

        var user = userOpt.get();
        boolean matches = hashingService.matches(command.password(), user.getPassword());
        if (!matches)
            throw new IllegalArgumentException("Invalid credentials");

        return user;
    }

    @Override
    public String handle(Verify2FACommand command) {
        var user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean valid = totpService.verifyCode(user.getTwoFactorSecret(), command.code());

        if (!valid)
            throw new IllegalArgumentException("Invalid 2FA code");

        if (!user.isTwoFactorEnabled()) {
            user.enableTwoFactor();
            userRepository.save(user);
        }

        return tokenService.generateToken(user.getEmail());
    }

    @Override
    public String handle(GenerateUserQrCommand command) {
        var user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean matches = hashingService.matches(command.password(), user.getPassword());
        if (!matches)
            throw new IllegalArgumentException("Invalid credentials");

        try {
            return totpService.generateQrCodeImage(user.getOrganizationName(), user.getTwoFactorSecret());
        } catch (Exception e) {
            throw new RuntimeException("Error generating QR", e);
        }
    }

}
