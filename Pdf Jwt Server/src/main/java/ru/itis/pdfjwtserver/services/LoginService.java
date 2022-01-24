package ru.itis.pdfjwtserver.services;


import ru.itis.pdfjwtserver.dto.AccessTokenDto;
import ru.itis.pdfjwtserver.dto.LoginPasswordDto;
import ru.itis.pdfjwtserver.dto.RefreshTokenDto;

public interface LoginService {

    RefreshTokenDto login(LoginPasswordDto emailPasswordDto);
    AccessTokenDto authenticate(RefreshTokenDto refreshTokenDto);

}
