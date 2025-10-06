package pe.healthsync.meditrack.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.healthsync.meditrack.iam.domain.model.commands.GenerateUserQrCommand;
import pe.healthsync.meditrack.iam.domain.services.UserCommandService;
import pe.healthsync.meditrack.iam.interfaces.rest.requests.GenerateQrRequest;
import pe.healthsync.meditrack.iam.interfaces.rest.requests.SignInRequest;
import pe.healthsync.meditrack.iam.interfaces.rest.requests.SignUpRequest;
import pe.healthsync.meditrack.iam.interfaces.rest.requests.VerifyTOTPRequest;
import pe.healthsync.meditrack.iam.interfaces.rest.responses.GenerateQrResponse;
import pe.healthsync.meditrack.iam.interfaces.rest.responses.TokenResponse;
import pe.healthsync.meditrack.iam.interfaces.rest.responses.SignUpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthController {
    @Autowired
    private UserCommandService userCommandService;

    @Operation(summary = "Register a new user", description = "Creates a new user and returns a QR code for 2FA setup.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = @Content(schema = @Schema(implementation = SignUpResponse.class)))
    })
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest req) {
        var command = req.toCommand();
        var user = userCommandService.handle(command);
        var qrCode = userCommandService.handle(new GenerateUserQrCommand(user.getEmail(), user.getPassword()));
        var registerResponse = new SignUpResponse(qrCode);
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Generate QR for user", description = "Generates or regenerates a QR code for a user's 2FA registration.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "QR generated", content = @Content(schema = @Schema(implementation = GenerateQrResponse.class)))
    })
    @PostMapping("/generate-qr")
    public ResponseEntity<GenerateQrResponse> generateQr(@RequestBody GenerateQrRequest req) {
        var qrCode = userCommandService.handle(new GenerateUserQrCommand(req.email(), req.password()));
        var response = new GenerateQrResponse(qrCode);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Verify TOTP code", description = "Verifies a TOTP code and returns an authentication token on success.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token returned", content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    })
    @PostMapping("/verify-totp")
    public ResponseEntity<TokenResponse> verifyTotp(@RequestBody VerifyTOTPRequest req) {
        var command = req.toCommand();
        var token = userCommandService.handle(command);
        var response = new TokenResponse(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Sign in a user", description = "Login endpoint that handles 2FA. Returns QR if 2FA not activated, or a message to enter TOTP code if 2FA is enabled.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "2FA not activated, QR generated", content = @Content(schema = @Schema(implementation = GenerateQrResponse.class))),
            @ApiResponse(responseCode = "200", description = "2FA enabled", content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest req) {
        var command = req.toCommand();
        var user = userCommandService.handle(command);

        if (!user.isTwoFactorEnabled()) {
            var qrCode = userCommandService.handle(new GenerateUserQrCommand(user.getEmail(), user.getPassword()));
            var response = new GenerateQrResponse(qrCode);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }

        return ResponseEntity.ok(null);
    }
}
