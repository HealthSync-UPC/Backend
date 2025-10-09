package pe.healthsync.meditrack.iam.domain.model.commands;

import java.util.regex.Pattern;

public record SignInCommand(
                String email,
                String password) {

        private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

        public SignInCommand {
                if (email == null || email.isBlank()) {
                        throw new IllegalArgumentException("Email must not be blank");
                }
                if (!EMAIL_PATTERN.matcher(email).matches()) {
                        throw new IllegalArgumentException("Email format is invalid");
                }
                if (password == null || password.isBlank()) {
                        throw new IllegalArgumentException("Password must not be blank");
                }
        }
}
