package com.litCitrus.zamongcampusServer.config;


import com.litCitrus.zamongcampusServer.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    private final FilterChannelInterceptor filterChannelInterceptor;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub"); // 구독하는 곳(prefix: sub / 원래 topic)
        config.setApplicationDestinationPrefixes("/pub"); // 메세지 보내는곳(prefix: pub 원래 app)
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // sockjs를 위해 https://localhost:8080를 allow해주는 것인데, 추후 적용
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("https://10.0.2.2:8080").setAllowedOrigins("https://localhost:8080").withSockJS(); // 연결하는곳
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(filterChannelInterceptor);
    }

}

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
class FilterChannelInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        System.out.println("full message:" + message);
        System.out.println("auth:" + headerAccessor.getNativeHeader("Authorization"));
        System.out.println("auth:" + headerAccessor.getFirstNativeHeader("Authorization"));
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            System.out.println("msg: " + "conne");
            System.out.println("stomp jwt token validation");
            tokenProvider.validateToken(headerAccessor.getFirstNativeHeader("Authorization").substring(7));
        }
        //throw new MessagingException("no permission! ");
        return message;
    }
}