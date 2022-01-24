package ru.itis.pdfserver.amqp;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import ru.itis.pdfserver.dto.PdfReportData;
import ru.itis.pdfserver.dto.PdfResponse;
import ru.itis.pdfserver.exceptions.BlockedAccountException;
import ru.itis.pdfserver.exceptions.FaultResponseException;
import ru.itis.pdfserver.security.TokenManager;

@Component
public class RabbitClient implements AmqpClient {

    protected final RabbitTemplate template;
    protected final AuthenticationMessagePostProcessor messagePostProcessor;
    protected final TokenManager tokenManager;

    protected final String JAVA_LAB_ROUTING_KEY;
    protected final String LINUX_ROUTING_KEY;
    protected final String REQUESTS_EXCHANGE_NAME;

    @Autowired
    public RabbitClient(RabbitTemplate template,
                        AuthenticationMessagePostProcessor messagePostProcessor,
                        @Value("${pdf.reports.java-lab.routing-key}") String javaLabRoutingKey,
                        @Value("${pdf.reports.linux.routing-key}") String linuxRoutingKey,
                        @Value("${pdf.reports.requests-exchange-name}") String requestsExchangeName,
                        TokenManager tokenManager
    ){
        template.setReplyTimeout(20000);
        this.template = template;
        this.messagePostProcessor = messagePostProcessor;
        JAVA_LAB_ROUTING_KEY = javaLabRoutingKey;
        LINUX_ROUTING_KEY = linuxRoutingKey;
        REQUESTS_EXCHANGE_NAME = requestsExchangeName;
        this.tokenManager = tokenManager;
    }

    @Override
    public byte[] handleJavaLabRequest(PdfReportData reportData) {
        return handleReportRequest(reportData, JAVA_LAB_ROUTING_KEY, false);
    }

    @Override
    public byte[] handleLinuxRequest(PdfReportData reportData) {
        return handleReportRequest(reportData, LINUX_ROUTING_KEY, false);
    }

    protected byte[] handleReportRequest(PdfReportData reportData, String routingKey, Boolean reCall){
        messagePostProcessor.setAccessToken(tokenManager.getAccessToken());
        CorrelationData correlationData = new CorrelationData();
        PdfResponse response =
                template.convertSendAndReceiveAsType(
                        REQUESTS_EXCHANGE_NAME,
                        routingKey,
                        reportData,
                        messagePostProcessor,
                        correlationData,
                        new ParameterizedTypeReference<PdfResponse>() {
                        });

        if (response != null){
            if (response.getStatus().equals(PdfResponse.Status.SUCCESS)){
                return response.getPdf();
            } else if ((response.getStatus().equals(PdfResponse.Status.EXPIRED_TOKEN))
                    || (response.getStatus().equals(PdfResponse.Status.INVALID_TOKEN))){
                tokenManager.dropAccessToken();
            } else if (response.getStatus().equals(PdfResponse.Status.BANNED_TOKEN)){
                throw new BlockedAccountException();
            } else {
                throw new FaultResponseException();
            }
        }
        if (reCall){
            throw new FaultResponseException();
        }
        return handleReportRequest(reportData, routingKey, true);
    }

}
