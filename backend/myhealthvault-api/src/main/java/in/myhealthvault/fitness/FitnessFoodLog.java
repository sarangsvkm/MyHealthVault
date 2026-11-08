package in.myhealthvault.fitness;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "fitness_food_log")
public class FitnessFoodLog {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false)
    private Integer calories;

    @Column(name = "meal_type", nullable = false, length = 50)
    private String mealType;

    @Column(name = "logged_at", nullable = false)
    private Instant loggedAt = Instant.now();

    protected FitnessFoodLog() {}

    public FitnessFoodLog(UUID userId, String name, String category, Integer calories, String mealType, Instant loggedAt) {
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.calories = calories;
        this.mealType = mealType;
        this.loggedAt = loggedAt != null ? loggedAt : Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Integer getCalories() { return calories; }
    public String getMealType() { return mealType; }
    public Instant getLoggedAt() { return loggedAt; }
}

