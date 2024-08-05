package cn.redcoral.messageplus;

import cn.redcoral.messageplus.config.MessagePlusConfig;
import cn.redcoral.messageplus.manage.MessagePlusUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadFactory;

@SpringBootTest(classes = {AopTest.class, MyAopTest.class, MessagePlusConfig.class})
@EnableMessagePlus
class ChatServiceApplicationTests {

    @Autowired
    private MyAopTest myAopTest;

    @Test
    void contextLoads() throws InterruptedException {
        ThreadFactory factory = new CustomizableThreadFactory();
        MessagePlusUtils messagePlusUtils = new MessagePlusUtils();
        for (int i = 0; i < 100; i++) {
            factory.newThread(() -> {
                messagePlusUtils.joinChat("1", null, new Session() {
                    public WebSocketContainer getContainer() {return null;}
                    public void addMessageHandler(MessageHandler messageHandler) throws IllegalStateException {}
                    public Set<MessageHandler> getMessageHandlers() {return null;}
                    public void removeMessageHandler(MessageHandler messageHandler) {}
                    public String getProtocolVersion() {return null;}
                    public String getNegotiatedSubprotocol() {return null;}
                    public List<Extension> getNegotiatedExtensions() {return null;}
                    public boolean isSecure() {return false;}
                    public boolean isOpen() {return false;}
                    public long getMaxIdleTimeout() {return 0;}
                    public void setMaxIdleTimeout(long l) {}
                    public void setMaxBinaryMessageBufferSize(int i) {}
                    public int getMaxBinaryMessageBufferSize() {return 0;}
                    public void setMaxTextMessageBufferSize(int i) {}
                    public int getMaxTextMessageBufferSize() {return 0;}
                    public RemoteEndpoint.Async getAsyncRemote() {return null;}
                    public RemoteEndpoint.Basic getBasicRemote() {return null;}
                    public String getId() {return null;}
                    public void close() throws IOException {}
                    public void close(CloseReason closeReason) throws IOException {}
                    public URI getRequestURI() {return null;}
                    public Map<String, List<String>> getRequestParameterMap() {return null;}
                    public String getQueryString() {return null;}
                    public Map<String, String> getPathParameters() {return null;}
                    public Map<String, Object> getUserProperties() {return null;}
                    public Principal getUserPrincipal() {return null;}
                    public Set<Session> getOpenSessions() {return null;}
                    public <T> void addMessageHandler(Class<T> aClass, MessageHandler.Partial<T> partial) throws IllegalStateException {}
                    public <T> void addMessageHandler(Class<T> aClass, MessageHandler.Whole<T> whole) throws IllegalStateException {}
                });
            }).start();
        }
        Thread.sleep(5000);
        System.out.println(messagePlusUtils.getOnLinePeopleNum());
    }

    @Test
    public void aopTest() {
        myAopTest.myPrintln(1);
    }

}
