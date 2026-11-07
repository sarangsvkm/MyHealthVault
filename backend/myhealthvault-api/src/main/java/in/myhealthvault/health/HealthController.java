package in.myhealthvault.health;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {
    private final HealthRecordRepository repo;
    private static final Set<String> PROVIDERS = Set.of("APPLE_HEALTH","HEALTH_CONNECT","MANUAL","KERALA_FIT");
    private static final Set<String> TYPES = Set.of("STEPS","HEART_RATE","WEIGHT","HEIGHT",
        "BLOOD_PRESSURE_SYSTOLIC","BLOOD_PRESSURE_DIASTOLIC","BLOOD_GLUCOSE","SLEEP_MINUTES",
        "CALORIES_BURNED","CALORIES_INTAKE","WATER_ML");

    public HealthController(HealthRecordRepository repo) { this.repo=repo; }

    public record RecordRequest(@NotBlank String provider, @NotBlank String type,
        @NotNull @DecimalMin("0") BigDecimal value, @NotBlank String unit,
        @NotNull Instant startTime, Instant endTime, String sourceRecordId) {}

    @PostMapping("/sync")
    @Transactional
    public ResponseEntity<?> sync(@Valid @RequestBody List<@Valid RecordRequest> records,
                                  Authentication auth) {
        if (records.size() > 1000) return ResponseEntity.badRequest().body(Map.of("error","Maximum 1000 records per batch"));
        UUID uid=UUID.fromString(auth.getName());
        int inserted=0, skipped=0;
        for (var r: records) {
            String provider=r.provider().toUpperCase(Locale.ROOT);
            String type=r.type().toUpperCase(Locale.ROOT);
            if (!PROVIDERS.contains(provider) || !TYPES.contains(type))
                return ResponseEntity.badRequest().body(Map.of("error","Unsupported provider or record type"));
            if (r.endTime()!=null && r.endTime().isBefore(r.startTime()))
                return ResponseEntity.badRequest().body(Map.of("error","Invalid time range"));
            int n=repo.insertIfNew(uid,provider,type,r.value(),r.unit(),r.startTime(),r.endTime(),r.sourceRecordId());
            if (n==1) inserted++; else skipped++;
        }
        return ResponseEntity.ok(Map.of("inserted",inserted,"skipped",skipped));
    }

    @GetMapping("/records")
    public List<HealthRecord> records(@RequestParam String type, Authentication auth) {
        return repo.findByUserIdAndRecordTypeOrderByStartTimeDesc(
            UUID.fromString(auth.getName()), type.toUpperCase(Locale.ROOT));
    }
}
