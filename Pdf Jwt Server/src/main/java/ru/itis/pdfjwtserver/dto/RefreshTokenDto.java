package ru.itis.pdfjwtserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.pdfjwtserver.models.RefreshToken;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDto {

    protected String token;
    protected Long expiredTime;

    public static RefreshTokenDto from(RefreshToken refreshToken){
        return RefreshTokenDto.builder()
                .token(refreshToken.getToken())
                .expiredTime(refreshToken.getExpiredTime().getTime())
                .build();
    }

}
