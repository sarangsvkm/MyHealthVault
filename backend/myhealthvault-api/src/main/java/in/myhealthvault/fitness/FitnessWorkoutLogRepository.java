package in.myhealthvault.fitness;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FitnessWorkoutLogRepository extends JpaRepository<FitnessWorkoutLog, UUID> {
    List<FitnessWorkoutLog> findByUserIdOrderByCompletedAtDesc(UUID userId);
}

