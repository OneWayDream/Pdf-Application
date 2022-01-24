package ru.itis.pdfgenerator.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.pdfgenerator.exceptions.BannedTokenException;
import ru.itis.pdfgenerator.exceptions.BannedUserException;
import ru.itis.pdfgenerator.exceptions.ExpiredJwtException;
import ru.itis.pdfgenerator.services.JwtBlacklistService;
import java.util.Date;

@Component
public class JwtAuthenticationManager {

    protected final JwtBlacklistService service;
    protected final String ACCESS_KEY;

    @Autowired
    public JwtAuthenticationManager(
            JwtBlacklistService jwtBlacklistService,
            @Value("${jwt.access-secret-key}") String accessSecretKey
                                    ){
        service = jwtBlacklistService;
        this.ACCESS_KEY = accessSecretKey;
    }

    public boolean handleToken(String token){

        boolean isCorrect = false;

        if (token!=null){

            if (service.exists(token)) {
                throw new BannedTokenException();
            }

            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(ACCESS_KEY))
                    .build()
                    .verify(token);

            Date date = java.util.Calendar.getInstance().getTime();
            Date timeToDie = decodedJWT.getClaim("expiration").asDate();
            if (!date.before(timeToDie)){
                throw new ExpiredJwtException();
            }
            isCorrect = true;
        }

        return isCorrect;
    }

}
