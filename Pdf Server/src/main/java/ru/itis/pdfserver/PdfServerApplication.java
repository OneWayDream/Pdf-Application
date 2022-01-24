package ru.itis.pdfserver;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PdfServerApplication {

    @Bean
    public DirectExchange requestsExchange(@Value("${pdf.reports.requests-exchange-name}") String requestsExchangeName) {
        return new DirectExchange(requestsExchangeName);
    }

    @Bean
    public MessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public static void main(String[] args) {
        SpringApplication.run(PdfServerApplication.class, args);
    }

}
