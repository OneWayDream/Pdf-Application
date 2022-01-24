package ru.itis.pdfjwtserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.pdfjwtserver.services.JwtBlacklistService;

@RequiredArgsConstructor
@RestController
@CrossOrigin
public class AdministrationController {

    protected final JwtBlacklistService jwtBlacklistService;

    @PostMapping("/ban-token")
    public ResponseEntity<?> login(@RequestBody String token){
        if (token == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
        try{
            jwtBlacklistService.add(token);
            return ResponseEntity.ok().build();
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                    .body("Something went wrong...");
        }
    }

}
