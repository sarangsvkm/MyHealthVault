package in.myhealthvault.abdm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/abdm")
public class AbdmController {
    private final String mode;
    public AbdmController(@Value("${app.abdm.mode:disabled}") String mode) { this.mode=mode; }

    @GetMapping("/status")
    public Map<String,String> status() {
        return Map.of("provider","ABDM","status",
            "enabled".equalsIgnoreCase(mode) ? "SANDBOX_CONFIGURED_PENDING_IMPLEMENTATION" : "NOT_CONFIGURED");
    }

    @PostMapping("/consent-requests")
    public ResponseEntity<?> createConsentRequest() {
        if (!"enabled".equalsIgnoreCase(mode))
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error","ABDM sandbox credentials and approved workflow are not configured"));
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
            .body(Map.of("error","Implement against the currently approved ABDM sandbox API specification before enabling this route"));
    }
}
