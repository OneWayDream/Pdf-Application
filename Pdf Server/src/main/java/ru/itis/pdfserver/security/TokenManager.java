package ru.itis.pdfserver.security;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.pdfserver.exceptions.AuthorizationFaultException;

import java.util.Date;

@Component
public class TokenManager {

    protected String refreshToken;
    protected String accessToken;
    protected Date refreshTokenExpiration;
    protected Date accessTokenExpiration;
    protected final String USER_NAME;
    protected final String PASSWORD;
    protected final String LOGIN_PARAM_NAME;
    protected final String PASSWORD_PARAM_NAME;
    protected final String TOKEN_SERVER_URL;
    protected final String TOKEN_REFRESH_URL;
    protected final String TOKEN_ACCESS_URL;
    protected final String REFRESH_TOKEN_PARAM_NAME;
    protected final String REFRESH_TOKEN_HEADER_NAME;
    protected final String ACCESS_TOKEN_PARAM_NAME;
    protected final String EXPIRATION_PARAM_NAME;

    protected OkHttpClient client;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Autowired
    public TokenManager(
            @Value("${security.jwt.user-name}") String userName,
            @Value("${security.jwt.password}") String password,
            @Value("${security.jwt.user-name-param-name}") String loginParamName,
            @Value("${security.jwt.password-param-name}") String passwordParamName,
            @Value("${security.jwt.token-server-host}") String serverHost,
            @Value("${security.jwt.token-server-port}") String serverPort,
            @Value("${security.jwt.token-server-refresh-url}") String refreshUrl,
            @Value("${security.jwt.token-server-access-url}") String accessUrl,
            @Value("${security.jwt.refresh-token-param-name}") String refreshParamName,
            @Value("${security.jwt.refresh-token-header-name}") String refreshHeaderName,
            @Value("${security.jwt.access-token-param-name}") String accessParamName,
            @Value("${security.jwt.expired-time-param-name}") String expiration
    ){
        client = new OkHttpClient();
        USER_NAME = userName;
        PASSWORD = password;
        LOGIN_PARAM_NAME = loginParamName;
        PASSWORD_PARAM_NAME = passwordParamName;
        TOKEN_SERVER_URL = serverHost + ":" + serverPort;
        TOKEN_REFRESH_URL = refreshUrl;
        TOKEN_ACCESS_URL = accessUrl;
        REFRESH_TOKEN_PARAM_NAME = refreshParamName;
        REFRESH_TOKEN_HEADER_NAME = refreshHeaderName;
        ACCESS_TOKEN_PARAM_NAME = accessParamName;
        EXPIRATION_PARAM_NAME = expiration;
    }

    public String getAccessToken(){
        Date date = java.util.Calendar.getInstance().getTime();
        if ((accessToken == null) || (!date.before(accessTokenExpiration))) {
            getRefreshToken();
            updateAccessToken();
        }
        return accessToken;
    }

    public void dropAccessToken(){
        this.accessToken = null;
    }

    public void dropRefreshToken(){
        this.refreshToken = null;
    }

    protected String getRefreshToken(){
        Date date = java.util.Calendar.getInstance().getTime();
        if ((refreshToken == null) || (!date.before(refreshTokenExpiration))){
            updateRefreshToken();
        }
        return refreshToken;
    }

    protected void updateAccessToken(){
        try{
            JSONObject body = new JSONObject();
            RequestBody requestBody = RequestBody.create(JSON, body.toString());
            Request request = new Request.Builder()
                    .url(TOKEN_SERVER_URL + TOKEN_ACCESS_URL)
                    .post(requestBody)
                    .header(REFRESH_TOKEN_HEADER_NAME, refreshToken)
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject jsonData = new JSONObject(response.body().string());
            accessToken = jsonData.getString(ACCESS_TOKEN_PARAM_NAME);
            accessTokenExpiration = new Date(jsonData.getLong(EXPIRATION_PARAM_NAME));
        } catch (Exception ex) {
            throw new AuthorizationFaultException(ex);
        }
    }

    protected void updateRefreshToken(){
        try{
            JSONObject body = new JSONObject()
                    .put(LOGIN_PARAM_NAME, USER_NAME)
                    .put(PASSWORD_PARAM_NAME, PASSWORD);
            RequestBody requestBody = RequestBody.create(JSON, body.toString());
            Request request = new Request.Builder()
                    .url(TOKEN_SERVER_URL + TOKEN_REFRESH_URL)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject jsonData = new JSONObject(response.body().string());
            refreshToken = jsonData.getString(REFRESH_TOKEN_PARAM_NAME);
            accessTokenExpiration = new Date(jsonData.getLong(EXPIRATION_PARAM_NAME));
        } catch (Exception ex) {
            throw new AuthorizationFaultException(ex);
        }
    }

}
