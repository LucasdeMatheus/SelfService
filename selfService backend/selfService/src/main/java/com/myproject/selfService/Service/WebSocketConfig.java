package com.myproject.selfService.Service;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SerialWebSocketHandler serialWebSocketHandler;

    public WebSocketConfig(SerialWebSocketHandler handler) {
        this.serialWebSocketHandler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(serialWebSocketHandler, "/ws/weight")
                .setAllowedOrigins("*"); // desenvolvimento
    }

    // ✅ Adicione este método
    public SerialWebSocketHandler getHandler() {
        return serialWebSocketHandler;
    }
}
