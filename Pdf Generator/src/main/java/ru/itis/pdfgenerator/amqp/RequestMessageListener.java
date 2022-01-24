package ru.itis.pdfgenerator.amqp;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.pdfgenerator.dto.PdfReportData;
import ru.itis.pdfgenerator.dto.PdfResponse;
import ru.itis.pdfgenerator.exceptions.BannedTokenException;
import ru.itis.pdfgenerator.exceptions.BannedUserException;
import ru.itis.pdfgenerator.exceptions.ExpiredJwtException;
import ru.itis.pdfgenerator.models.StatisticalData;
import ru.itis.pdfgenerator.security.JwtAuthenticationManager;
import ru.itis.pdfgenerator.services.StatisticsService;
import ru.itis.pdfgenerator.utils.PdfGenerator;

@Component
public class RequestMessageListener {

    protected final StatisticsService statisticsService;
    protected final String PATTERN_TYPE_NAME;
    protected final JwtAuthenticationManager jwtAuthenticationManager;
    protected final PdfGenerator pdfGenerator;

    @Autowired
    public RequestMessageListener(
            StatisticsService statisticsService,
            @Value("${statistics.pattern-type-name}") String patternTypeName,
            JwtAuthenticationManager jwtAuthenticationManager,
            PdfGenerator pdfGenerator
    ){
        this.statisticsService = statisticsService;
        PATTERN_TYPE_NAME = patternTypeName;
        this.jwtAuthenticationManager = jwtAuthenticationManager;
        this.pdfGenerator = pdfGenerator;
    }

    @RabbitListener(queues = "#{queue.name}")
    public PdfResponse receive(PdfReportData reportData, Message message) {
        try{
            if (handleAuthorization(message)){

                byte[] pdf = pdfGenerator.generatePdf(reportData);

                statisticsService.increaseCounterForPattern(StatisticalData.PatternType.valueOf(PATTERN_TYPE_NAME), 1L);

                return PdfResponse.builder()
                        .status(PdfResponse.Status.SUCCESS)
                        .pdf(pdf)
                        .build();
            } else {
                return null;
            }
        } catch (BannedTokenException ex){
            return PdfResponse.builder()
                    .status(PdfResponse.Status.BANNED_TOKEN)
                    .build();
        } catch (ExpiredJwtException ex) {
            return PdfResponse.builder()
                    .status(PdfResponse.Status.EXPIRED_TOKEN)
                    .build();
        } catch (JWTVerificationException ex){
            return PdfResponse.builder()
                    .status(PdfResponse.Status.INVALID_TOKEN)
                    .build();
        } catch (Exception ex){
            return PdfResponse.builder()
                    .status(PdfResponse.Status.FAULT)
                    .build();
        }
    }

    protected boolean handleAuthorization(Message message){
        return jwtAuthenticationManager.handleToken(message.getMessageProperties().getHeader("JWT-access"));
    }


}
