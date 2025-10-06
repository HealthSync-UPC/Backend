package pe.healthsync.meditrack.iam.interfaces.rest.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenResponse(
        /*
         * String message,
         * String qrCode,
         */
        String token
/* String email */) {
}
