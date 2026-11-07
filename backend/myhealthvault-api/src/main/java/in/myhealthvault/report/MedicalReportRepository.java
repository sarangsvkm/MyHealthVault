package in.myhealthvault.report;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MedicalReportRepository extends JpaRepository<MedicalReport,UUID> {
    List<MedicalReport> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<MedicalReport> findByIdAndUserId(UUID id, UUID userId);
}
