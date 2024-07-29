//package cn.redcoral.messageplus.handler;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@Component
//@Scope("prototype")
//public class ChatWebSocketHandler extends TextWebSocketHandler {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    @Autowired
//    public ChatWebSocketHandler(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        super.afterConnectionEstablished(session);
//        System.out.println("New WebSocket connection established");
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        System.out.println("Received message: " + payload);
//
//        // 广播消息到/topic/chat
//        messagingTemplate.convertAndSend("/topic/chat", payload);
//    }
//}
