package in.myhealthvault.health;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.*;
import java.time.Instant;
import java.math.BigDecimal;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, UUID> {
    List<HealthRecord> findByUserIdAndRecordTypeOrderByStartTimeDesc(UUID userId, String recordType);
    boolean existsByUserIdAndProviderAndSourceRecordId(UUID userId, String provider, String sourceRecordId);

    @Modifying
    @Query(value = "INSERT INTO health_record(id,user_id,provider,record_type,value,unit,start_time,end_time,source_record_id) " +
      "VALUES(gen_random_uuid(),:uid,:provider,:type,:value,:unit,:start,:end,:source) " +
      "ON CONFLICT (user_id,provider,source_record_id) WHERE source_record_id IS NOT NULL DO NOTHING",
      nativeQuery = true)
    int insertIfNew(@Param("uid") UUID uid, @Param("provider") String provider,
      @Param("type") String type, @Param("value") BigDecimal value, @Param("unit") String unit,
      @Param("start") Instant start, @Param("end") Instant end, @Param("source") String source);
}
