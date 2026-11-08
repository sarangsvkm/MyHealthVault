package in.myhealthvault.fitness;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "fitness_workout_log")
public class FitnessWorkoutLog {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "plan_name", nullable = false, length = 255)
    private String planName;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "completed_at", nullable = false)
    private Instant completedAt = Instant.now();

    protected FitnessWorkoutLog() {}

    public FitnessWorkoutLog(UUID userId, String planName, Integer durationMinutes, Instant completedAt) {
        this.userId = userId;
        this.planName = planName;
        this.durationMinutes = durationMinutes;
        this.completedAt = completedAt != null ? completedAt : Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getPlanName() { return planName; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public Instant getCompletedAt() { return completedAt; }
}

