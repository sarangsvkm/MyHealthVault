package in.myhealthvault.auth;

import in.myhealthvault.user.*;
import in.myhealthvault.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/auth", "/api/auth"})
public class AuthController {
    private final AppUserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(AppUserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users; this.encoder = encoder; this.jwt = jwt;
    }

    public record RegisterRequest(@Email @NotBlank String email,
        @Size(min=6, max=72) String password, @NotBlank @Size(max=120) String displayName) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        String email = req.email().toLowerCase().trim();
        if (users.existsByEmail(email)) return ResponseEntity.status(409).body(Map.of("error","Email already registered"));
        var user = users.save(new AppUser(email, encoder.encode(req.password()), req.displayName()));
        return ResponseEntity.status(201).body(Map.of("accessToken", jwt.issue(user.getId()),
            "tokenType","Bearer","userId",user.getId(),"displayName",user.getDisplayName()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        var user = users.findByEmail(req.email().toLowerCase().trim()).orElse(null);
        if (user == null || !encoder.matches(req.password(), user.getPasswordHash()))
            return ResponseEntity.status(401).body(Map.of("error","Invalid email or password"));
        return ResponseEntity.ok(Map.of("accessToken", jwt.issue(user.getId()),
            "tokenType","Bearer","userId",user.getId(),"displayName",user.getDisplayName()));
    }
}
