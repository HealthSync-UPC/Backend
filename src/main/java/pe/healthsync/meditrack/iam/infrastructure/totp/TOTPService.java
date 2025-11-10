package pe.healthsync.meditrack.iam.infrastructure.totp;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;

@Service
public class TOTPService {
    private final SecretGenerator secretGenerator = new DefaultSecretGenerator();

    private final QrGenerator qrGenerator = new ZxingPngQrGenerator();

    private final CodeVerifier verifier;

    public TOTPService() {
        DefaultCodeVerifier v = new DefaultCodeVerifier(
                new DefaultCodeGenerator(HashingAlgorithm.SHA1),
                new SystemTimeProvider());
        v.setAllowedTimePeriodDiscrepancy(2);
        this.verifier = v;
    }

    @Value("${spring.application.name}")
    private String appName;

    public String generateSecret() {
        return secretGenerator.generate();
    }

    public String generateQrCodeImage(String organizationName, String email, String secret)
            throws QrGenerationException {
        var label = email + " (" + organizationName + ")";

        QrData data = new QrData.Builder()
                .label(label)
                .secret(secret)
                .issuer(appName)
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        byte[] imageData = qrGenerator.generate(data);
        String mimeType = qrGenerator.getImageMimeType();

        return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(imageData);
    }

    public boolean verifyCode(String secret, String code) {
        try {
            var expectedCode = new DefaultCodeGenerator(HashingAlgorithm.SHA1)
                    .generate(secret, new SystemTimeProvider().getTime() / 30);
            System.out.println("Expected code (server): " + expectedCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return verifier.isValidCode(secret, code);
    }
}
