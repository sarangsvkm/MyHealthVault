package in.myhealthvault.health;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="health_record")
public class HealthRecord {
    @Id @GeneratedValue private UUID id;
    @Column(name="user_id", nullable=false) private UUID userId;
    @Column(nullable=false) private String provider;
    @Column(name="record_type", nullable=false) private String recordType;
    @Column(nullable=false, precision=20, scale=6) private BigDecimal value;
    @Column(nullable=false) private String unit;
    @Column(name="start_time", nullable=false) private Instant startTime;
    @Column(name="end_time") private Instant endTime;
    @Column(name="source_record_id") private String sourceRecordId;

    protected HealthRecord() {}
    public HealthRecord(UUID userId, String provider, String recordType, BigDecimal value,
        String unit, Instant startTime, Instant endTime, String sourceRecordId) {
        this.userId=userId; this.provider=provider; this.recordType=recordType;
        this.value=value; this.unit=unit; this.startTime=startTime;
        this.endTime=endTime; this.sourceRecordId=sourceRecordId;
    }
    public UUID getId(){return id;}
    public String getRecordType(){return recordType;}
    public BigDecimal getValue(){return value;}
    public String getUnit(){return unit;}
    public Instant getStartTime(){return startTime;}
    public String getProvider(){return provider;}
}
