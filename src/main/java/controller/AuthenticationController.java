package controller;

import dto.auth.AuthCreateUserRequest;
import dto.auth.AuthLoginRequest;
import dto.auth.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.impl.UserDetailsServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CompromisedPasswordChecker passwordChecker;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest userRequest) {
        //chequea en una pagina web que tantas veces fue usada esta constrasenia con lo cual ve que tan comprometida esta
        CompromisedPasswordDecision decision = passwordChecker.check(userRequest.password());

        if (decision.isCompromised()) {
            throw new IllegalArgumentException("Compromised Password.");
        }

        return new ResponseEntity<>(userDetailsService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }
}

