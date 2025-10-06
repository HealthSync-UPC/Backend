package pe.healthsync.meditrack.iam.domain.model.commands;

import java.util.regex.Pattern;

public record SignUpCommand(
        String email,
        String password,
        String organizationName) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public SignUpCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email format is invalid");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be blank");
        }
        if (organizationName == null || organizationName.isBlank()) {
            throw new IllegalArgumentException("Organization name must not be blank");
        }
    }
}
