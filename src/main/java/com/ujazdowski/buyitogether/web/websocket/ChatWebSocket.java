package com.ujazdowski.buyitogether.web.websocket;

import com.ujazdowski.buyitogether.domain.Message;
import com.ujazdowski.buyitogether.service.ChatService;
import com.ujazdowski.buyitogether.web.rest.vm.MessageContentVM;
import com.ujazdowski.buyitogether.web.rest.vm.MessageVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Controller
public class ChatWebSocket {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);

    private final ChatService chatService;

    @Autowired
    public ChatWebSocket(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat/send/{chat}")
    public MessageVM sendMessage(@DestinationVariable("chat") Long chat, @Payload MessageContentVM message) {
        MessageVM messageVM = messageToVm(this.chatService.sendMessage(chat, message.getMessage()));
        return messageVM;
    }

    private MessageVM messageToVm(Message message) {
        return new MessageVM(message.getUser().getEmail(), message.getMessage(), message.getDate());
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }
}
