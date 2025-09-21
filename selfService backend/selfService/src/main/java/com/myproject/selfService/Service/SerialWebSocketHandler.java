package com.myproject.selfService.Service;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
public class SerialWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("WebSocket conectado: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("WebSocket desconectado: " + session.getId());
    }

    public void sendWeight(double weightKg) {
        double value = weightKg * 10; // cálculo do valor
        String payload = String.format(Locale.US, "{\"weight\": %.2f, \"value\": %.2f}", weightKg, value);


        synchronized (sessions) {
            for (WebSocketSession session : sessions) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(payload));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
