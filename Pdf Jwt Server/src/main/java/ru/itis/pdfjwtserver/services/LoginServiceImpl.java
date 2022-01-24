package ru.itis.pdfjwtserver.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.pdfjwtserver.dto.AccessTokenDto;
import ru.itis.pdfjwtserver.dto.LoginPasswordDto;
import ru.itis.pdfjwtserver.dto.RefreshTokenDto;
import ru.itis.pdfjwtserver.exceptions.BannedTokenException;
import ru.itis.pdfjwtserver.exceptions.BannedUserException;
import ru.itis.pdfjwtserver.exceptions.IncorrectJwtException;
import ru.itis.pdfjwtserver.exceptions.IncorrectUserDataException;
import ru.itis.pdfjwtserver.models.DataAccessUser;
import ru.itis.pdfjwtserver.models.RefreshToken;
import ru.itis.pdfjwtserver.redis.services.RedisUsersService;
import ru.itis.pdfjwtserver.repositories.RefreshTokensRepository;
import ru.itis.pdfjwtserver.repositories.UsersRepository;

import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    protected final RefreshTokensRepository refreshTokensRepository;
    protected final UsersRepository usersRepository;
    protected final PasswordEncoder passwordEncoder;
    private final RedisUsersService redisUsersService;
    protected final JwtBlacklistService jwtBlacklistService;

    protected String refreshSecretKey;

    @Value("${jwt.access-secret-key}")
    protected String accessSecretKey;

    @Value("${jwt.refresh-token-lifetime}")
    protected Long refreshTokenLifetime;

    @Value("${jwt.access-token-lifetime}")
    protected Long accessTokenLifetime;

    @Autowired
    public LoginServiceImpl(
            RefreshTokensRepository refreshTokensRepository,
            UsersRepository usersRepository,
            PasswordEncoder passwordEncoder,
            RedisUsersService redisUsersService,
            JwtBlacklistService jwtBlacklistService,
            @Value("${jwt.refresh-secret-key}") String refreshSecretKey,
            @Value("${jwt.access-secret-key}") String accessSecretKey,
            @Value("${jwt.refresh-token-lifetime}") Long refreshTokenLifetime,
            @Value("${jwt.access-token-lifetime}") Long accessTokenLifetime
    ){
        this.refreshTokensRepository = refreshTokensRepository;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisUsersService = redisUsersService;
        this.refreshSecretKey = refreshSecretKey;
        this.jwtBlacklistService = jwtBlacklistService;
        this.accessSecretKey = accessSecretKey;
        this.refreshTokenLifetime = refreshTokenLifetime;
        this.accessTokenLifetime = accessTokenLifetime;
    }

    @Override
    public RefreshTokenDto login(LoginPasswordDto loginPasswordDto) {
        DataAccessUser user = usersRepository.findByLogin(loginPasswordDto.getLogin()).orElseThrow(IncorrectUserDataException::new);
        if (user.getState().equals(DataAccessUser.State.BANNED)){
            throw new BannedUserException();
        }
        if (passwordEncoder.matches(loginPasswordDto.getPassword(), user.getHashPassword())){
            Date date = java.util.Calendar.getInstance().getTime();
            date.setTime(date.getTime() + refreshTokenLifetime);
            String token = JWT.create()
                    .withSubject(user.getId().toString())
                    .withClaim("id", user.getId())
                    .withClaim("login", user.getLogin())
                    .withClaim("state", user.getState().toString())
                    .withClaim("role", user.getRole().toString())
                    .withClaim("expiration", date)
                    .sign(Algorithm.HMAC256(refreshSecretKey));
            RefreshToken refreshToken = RefreshToken.builder()
                    .token(token)
                    .user(user)
                    .expiredTime(date)
                    .build();

            redisUsersService.addTokenToUser(user, token);

            return RefreshTokenDto.from(refreshToken);
        } else {
            throw new IncorrectUserDataException();
        }
    }

    @Override
    public AccessTokenDto authenticate(RefreshTokenDto refreshTokenDto) {
        if (jwtBlacklistService.exists(refreshTokenDto.getToken())){
            throw new BannedTokenException();
        }
        try{
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(refreshSecretKey))
                    .build()
                    .verify(refreshTokenDto.getToken());
            Date date = java.util.Calendar.getInstance().getTime();
            date.setTime(date.getTime() + accessTokenLifetime);
            String accessToken = JWT.create()
                    .withSubject(decodedJWT.getSubject())
                    .withClaim("login", decodedJWT.getClaim("login").asString())
                    .withClaim("role", decodedJWT.getClaim("role").asString())
                    .withClaim("state", decodedJWT.getClaim("state").asString())
                    .withClaim("expiration", date)
                    .sign(Algorithm.HMAC256(accessSecretKey));

            redisUsersService.addTokenToUser(usersRepository.findByLogin(decodedJWT.getClaim("login").asString())
                    .orElseThrow(IncorrectUserDataException::new), accessToken);

            return AccessTokenDto.builder()
                    .token(accessToken)
                    .expiredTime(decodedJWT.getClaim("expiration").asDate().getTime())
                    .build();
        } catch (JWTVerificationException ex) {
            throw new IncorrectJwtException(ex);
        }
    }

}
