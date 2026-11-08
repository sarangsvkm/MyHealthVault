package in.myhealthvault.fitness;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FitnessFoodLogRepository extends JpaRepository<FitnessFoodLog, UUID> {
    List<FitnessFoodLog> findByUserIdOrderByLoggedAtDesc(UUID userId);
}
