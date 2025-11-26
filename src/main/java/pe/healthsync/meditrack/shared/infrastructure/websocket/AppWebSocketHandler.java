package pe.healthsync.meditrack.shared.infrastructure.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class AppWebSocketHandler extends TextWebSocketHandler {
    private final ConcurrentHashMap<String, Set<WebSocketSession>> sessionsByEmail = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<WebSocketSession>> sessionsByDomain = new ConcurrentHashMap<>();

    private String extractDomain(String email) {
        int at = email.indexOf('@');
        return at >= 0 ? email.substring(at + 1).toLowerCase() : "";
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Object emailObj = session.getAttributes().get("email");
        if (emailObj == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("No email found"));
            return;
        }
        String email = emailObj.toString().toLowerCase();
        String domain = extractDomain(email);

        sessionsByEmail.computeIfAbsent(email, k -> new CopyOnWriteArraySet<>()).add(session);
        sessionsByDomain.computeIfAbsent(domain, k -> new CopyOnWriteArraySet<>()).add(session);

        session.sendMessage(new TextMessage("{\"type\":\"connected\",\"email\":\"" + email + "\"}"));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("{\"echo\":" + message.getPayload() + "}"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Object emailObj = session.getAttributes().get("email");
        if (emailObj != null) {
            String email = emailObj.toString().toLowerCase();
            String domain = extractDomain(email);

            Set<WebSocketSession> sByEmail = sessionsByEmail.get(email);
            if (sByEmail != null) {
                sByEmail.remove(session);
                if (sByEmail.isEmpty())
                    sessionsByEmail.remove(email);
            }

            Set<WebSocketSession> sByDomain = sessionsByDomain.get(domain);
            if (sByDomain != null) {
                sByDomain.remove(session);
                if (sByDomain.isEmpty())
                    sessionsByDomain.remove(domain);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        try {
            session.close(CloseStatus.SERVER_ERROR);
        } catch (IOException ignored) {
        }
    }

    public void sendToEmail(String email, String payload) {
        Set<WebSocketSession> sessions = sessionsByEmail.get(email.toLowerCase());
        if (sessions == null)
            return;
        TextMessage msg = new TextMessage(payload);
        sessions.forEach(s -> {
            try {
                if (s.isOpen())
                    s.sendMessage(msg);
            } catch (IOException e) {
            }
        });
    }

    public void sendToDomain(String domain, String payload) {
        Set<WebSocketSession> sessions = sessionsByDomain.get(domain.toLowerCase());
        if (sessions == null)
            return;
        TextMessage msg = new TextMessage(payload);
        sessions.forEach(s -> {
            try {
                if (s.isOpen())
                    s.sendMessage(msg);
            } catch (IOException e) {
            }
        });
    }
}
