package dasturlash.uz.controller;

import dasturlash.uz.dto.AppResponse;
import dasturlash.uz.dto.auth.AuthDTO;
import dasturlash.uz.dto.auth.RegistrationDTO;
import dasturlash.uz.dto.profile.ProfileDTO;
import dasturlash.uz.enums.AppLanguage;
import dasturlash.uz.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<AppResponse<String>> registration(@Valid @RequestBody RegistrationDTO dto,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang) {
        log.info("Registration: email: {}", dto.getEmail());
        return ResponseEntity.ok().body(authService.registration(dto, lang));
    }

    @GetMapping("/registration/email-verification/{token}")
    public ResponseEntity<String> emailVerification(@PathVariable("token") String token, @RequestParam(value = "lang", defaultValue = "UZ") AppLanguage lang) {
        log.info("Registration email verification:  token: {} ", token);
        return ResponseEntity.ok().body(authService.registrationEmailVerification(token, lang));
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody AuthDTO dto) {
        log.info("Login: {} ", dto.getEmail());
        return ResponseEntity.ok().body(authService.login(dto));
    }
}
