package ru.itis.pdfjwtserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.pdfjwtserver.dto.LoginPasswordDto;
import ru.itis.pdfjwtserver.dto.RefreshTokenDto;
import ru.itis.pdfjwtserver.exceptions.BannedTokenException;
import ru.itis.pdfjwtserver.exceptions.BannedUserException;
import ru.itis.pdfjwtserver.exceptions.IncorrectJwtException;
import ru.itis.pdfjwtserver.exceptions.IncorrectUserDataException;
import ru.itis.pdfjwtserver.services.LoginService;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class LoginController {

    protected final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginPasswordDto loginPasswordDto){
        try{
            return ResponseEntity.ok(loginService.login(loginPasswordDto));
        } catch (IncorrectUserDataException ex){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Incorrect user data.");
        } catch (BannedUserException ex){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("You got banned!");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
    }

    @PostMapping(
            value = "/auth",
            headers = {"JWT"}
    )
    public ResponseEntity<?> getAccessToken(HttpServletRequest request){
        try{
            return ResponseEntity.ok(loginService.authenticate(RefreshTokenDto.builder()
                    .token(request.getHeader("JWT"))
                    .build()));
        } catch (IncorrectJwtException ex){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Incorrect JWT token.");
        } catch (BannedTokenException ex){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("This token is banned :c");
        }
        catch (Exception ex){
                return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
        }
    }

}
