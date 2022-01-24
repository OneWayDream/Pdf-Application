package ru.itis.pdfserver.amqp;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Correlation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMessagePostProcessor implements MessagePostProcessor {

    protected String accessToken;

    public void setAccessToken(String token){
        this.accessToken = token;
    }

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties().setHeader("JWT-access", accessToken);
        return message;
    }

    @Override
    public Message postProcessMessage(Message message, Correlation correlation) {
        return MessagePostProcessor.super.postProcessMessage(message, correlation);
    }

    @Override
    public Message postProcessMessage(Message message, Correlation correlation, String exchange, String routingKey) {
        return MessagePostProcessor.super.postProcessMessage(message, correlation, exchange, routingKey);
    }
}
