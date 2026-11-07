package in.myhealthvault.report;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="medical_report")
public class MedicalReport {
    @Id @GeneratedValue private UUID id;
    @Column(name="user_id",nullable=false) private UUID userId;
    @Column(name="report_name",nullable=false) private String reportName;
    @Column(name="report_type") private String reportType;
    @Column(name="original_file_name",nullable=false) private String originalFileName;
    @Column(name="storage_key",nullable=false,unique=true) private String storageKey;
    @Column(name="mime_type",nullable=false) private String mimeType;
    @Column(name="file_size",nullable=false) private long fileSize;
    @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
    protected MedicalReport() {}
    public MedicalReport(UUID uid,String name,String original,String key,String mime,long size) {
      userId=uid; reportName=name; originalFileName=original; storageKey=key; mimeType=mime; fileSize=size;
    }
    public UUID getId(){return id;}
    public UUID getUserId(){return userId;}
    public String getReportName(){return reportName;}
    public String getOriginalFileName(){return originalFileName;}
    public String getStorageKey(){return storageKey;}
    public String getMimeType(){return mimeType;}
    public long getFileSize(){return fileSize;}
    public Instant getCreatedAt(){return createdAt;}
}
