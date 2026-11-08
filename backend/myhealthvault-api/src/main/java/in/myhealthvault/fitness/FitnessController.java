package in.myhealthvault.fitness;

import in.myhealthvault.health.HealthRecordRepository;
import in.myhealthvault.report.MedicalReportRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping({"/api/v1/fitness", "/api/tracker"})
public class FitnessController {

    private final FitnessFoodLogRepository foodRepo;
    private final FitnessWorkoutLogRepository workoutRepo;
    private final HealthRecordRepository healthRepo;
    private final MedicalReportRepository reportRepo;

    public FitnessController(FitnessFoodLogRepository foodRepo,
                             FitnessWorkoutLogRepository workoutRepo,
                             HealthRecordRepository healthRepo,
                             MedicalReportRepository reportRepo) {
        this.foodRepo = foodRepo;
        this.workoutRepo = workoutRepo;
        this.healthRepo = healthRepo;
        this.reportRepo = reportRepo;
    }

    public record FoodRequest(String name, String category, Integer calories, String mealType) {}
    public record WorkoutRequest(String planName, Integer durationMinutes) {}

    // ─── FOOD LOGS ─────────────────────────────────────────────────────────────
    @GetMapping("/food")
    public List<FitnessFoodLog> getFoodLogs(Authentication auth) {
        UUID uid = UUID.fromString(auth.getName());
        return foodRepo.findByUserIdOrderByLoggedAtDesc(uid);
    }

    @PostMapping("/food")
    @Transactional
    public ResponseEntity<FitnessFoodLog> addFoodLog(@RequestBody FoodRequest req, Authentication auth) {
        UUID uid = UUID.fromString(auth.getName());
        FitnessFoodLog log = new FitnessFoodLog(
            uid,
            req.name() != null ? req.name() : "Kerala Meal",
            req.category() != null ? req.category() : "General",
            req.calories() != null ? req.calories() : 0,
            req.mealType() != null ? req.mealType() : "Breakfast",
            Instant.now()
        );
        FitnessFoodLog saved = foodRepo.save(log);

        // Also track in health_record for unified vitals
        healthRepo.insertIfNew(
            uid,
            "KERALA_FIT",
            "CALORIES_INTAKE",
            java.math.BigDecimal.valueOf(saved.getCalories()),
            "kcal",
            saved.getLoggedAt(),
            null,
            "kf_food_" + saved.getId()
        );

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/food/{id}")
    @Transactional
    public ResponseEntity<?> deleteFoodLog(@PathVariable UUID id, Authentication auth) {
        foodRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Food log deleted"));
    }

    // ─── WORKOUT LOGS ──────────────────────────────────────────────────────────
    @GetMapping("/workout")
    public List<FitnessWorkoutLog> getWorkoutLogs(Authentication auth) {
        UUID uid = UUID.fromString(auth.getName());
        return workoutRepo.findByUserIdOrderByCompletedAtDesc(uid);
    }

    @PostMapping("/workout")
    @Transactional
    public ResponseEntity<FitnessWorkoutLog> addWorkoutLog(@RequestBody WorkoutRequest req, Authentication auth) {
        UUID uid = UUID.fromString(auth.getName());
        FitnessWorkoutLog log = new FitnessWorkoutLog(
            uid,
            req.planName() != null ? req.planName() : "Kerala Workout Routine",
            req.durationMinutes() != null ? req.durationMinutes() : 20,
            Instant.now()
        );
        FitnessWorkoutLog saved = workoutRepo.save(log);

        // Also track calories burned in health_record (~6 kcal per minute estimated)
        int estimatedBurn = saved.getDurationMinutes() * 6;
        healthRepo.insertIfNew(
            uid,
            "KERALA_FIT",
            "CALORIES_BURNED",
            java.math.BigDecimal.valueOf(estimatedBurn),
            "kcal",
            saved.getCompletedAt(),
            null,
            "kf_workout_" + saved.getId()
        );

        return ResponseEntity.ok(saved);
    }

    // ─── UNIFIED HEALTH & FITNESS SUMMARY ─────────────────────────────────────
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary(Authentication auth) {
        UUID uid = UUID.fromString(auth.getName());
        var foodLogs = foodRepo.findByUserIdOrderByLoggedAtDesc(uid);
        var workoutLogs = workoutRepo.findByUserIdOrderByCompletedAtDesc(uid);
        var reports = reportRepo.findAllByUserIdOrderByCreatedAtDesc(uid);

        int totalCaloriesLogged = foodLogs.stream().mapToInt(FitnessFoodLog::getCalories).sum();
        int totalWorkouts = workoutLogs.size();
        int totalReportsInVault = reports.size();

        Map<String, Object> summary = new HashMap<>();
        summary.put("userId", uid.toString());
        summary.put("totalCaloriesLogged", totalCaloriesLogged);
        summary.put("recentFoodLogsCount", foodLogs.size());
        summary.put("totalWorkoutsCompleted", totalWorkouts);
        summary.put("medicalReportsCount", totalReportsInVault);
        summary.put("vaultConnected", true);
        summary.put("platform", "Unified MyHealthVault & Kerala Fit Platform");

        return ResponseEntity.ok(summary);
    }
}
