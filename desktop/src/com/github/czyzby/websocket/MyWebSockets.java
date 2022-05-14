package com.github.czyzby.websocket;

import com.github.czyzby.websocket.WebSockets.WebSocketFactory;
import com.github.czyzby.websocket.impl.MyNvWebSocket;

public class MyWebSockets {

    public static void initiate() {
        WebSockets.FACTORY = new MyWebSocketFactory();
    }

    protected static class MyWebSocketFactory implements WebSocketFactory {
        @Override
        public WebSocket newWebSocket(final String url) {
            return new MyNvWebSocket(url);
        }
    }
}
