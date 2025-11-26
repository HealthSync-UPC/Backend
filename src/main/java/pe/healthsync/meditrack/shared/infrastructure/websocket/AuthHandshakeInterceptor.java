package pe.healthsync.meditrack.shared.infrastructure.websocket;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import pe.healthsync.meditrack.iam.application.outboundservices.tokens.TokenService;

@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            org.springframework.web.socket.WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        String token = null;
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            token = req.getParameter("token");
        } else {
            URI uri = request.getURI();
            String query = uri.getQuery();
            if (query != null) {
                for (String part : query.split("&")) {
                    String[] kv = part.split("=", 2);
                    if (kv.length == 2 && "token".equals(kv[0])) {
                        token = java.net.URLDecoder.decode(kv[1], java.nio.charset.StandardCharsets.UTF_8);
                        break;
                    }
                }
            }
        }

        if (token == null || token.isBlank()) {
            return false;
        }

        String email = tokenService.getEmailFromToken(token);
        if (email == null) {
            return false;
        }

        attributes.put("email", email);

        attributes.put("principal", new Principal() {
            @Override
            public String getName() {
                return email;
            }
        });

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            org.springframework.web.socket.WebSocketHandler wsHandler,
            Exception exception) {

    }
}
