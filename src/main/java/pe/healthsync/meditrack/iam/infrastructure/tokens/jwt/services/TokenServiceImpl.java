package pe.healthsync.meditrack.iam.infrastructure.tokens.jwt.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import pe.healthsync.meditrack.iam.infrastructure.tokens.jwt.BearerTokenService;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenServiceImpl implements BearerTokenService {
    private final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    private static final JWTCreator.Builder jwt = JWT.create();
    private Algorithm algorithm;

    private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    private static final int TOKEN_BEGIN_INDEX = 7;

    @Value("${authorization.jwt.secret}")
    private String secret;

    @Value("${authorization.jwt.expiration.days}")
    private int expirationDays;

    @Value("${spring.application.name}")
    private String issuer;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    @Override
    public String generateToken(String email, String role, Long id) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(expirationDays, ChronoUnit.DAYS);

        return jwt.withIssuer(issuer)
                .withSubject(email)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withClaim("role", role)
                .withClaim("id", id)
                .sign(algorithm);
    }

    @Override
    public String getEmailFromToken(String token) {
        return JWT.decode(token).getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            LOGGER.error("Error validating token", e);
            return false;
        }
    }

    private boolean isTokenPresentIn(String authorizationParameter) {
        return StringUtils.hasText(authorizationParameter);
    }

    private boolean isBearerTokenIn(String authorizationParameter) {
        return authorizationParameter.startsWith(BEARER_TOKEN_PREFIX);
    }

    private String extractTokenFrom(String authorizationHeaderParameter) {
        return authorizationHeaderParameter.substring(TOKEN_BEGIN_INDEX);
    }

    private String getAuthorizationParameterFrom(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_PARAMETER_NAME);
    }

    @Override
    public String getBearerTokenFrom(HttpServletRequest request) {
        String parameter = getAuthorizationParameterFrom(request);
        if (isTokenPresentIn(parameter) && isBearerTokenIn(parameter))
            return extractTokenFrom(parameter);
        return null;
    }
}