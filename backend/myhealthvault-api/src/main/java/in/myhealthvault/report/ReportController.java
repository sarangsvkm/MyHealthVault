package in.myhealthvault.report;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.nio.file.*;
import java.util.*;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final MedicalReportRepository repo;
    private final Path storage;
    private final long maxBytes;

    public ReportController(MedicalReportRepository repo,
        @Value("${app.storage.directory}") String directory,
        @Value("${app.storage.max-file-bytes:20971520}") long maxBytes) throws Exception {
        this.repo=repo; this.storage=Paths.get(directory).toAbsolutePath().normalize();
        this.maxBytes=maxBytes; Files.createDirectories(this.storage);
    }

    @PostMapping(value="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file, Authentication auth) throws Exception {
        if (file.isEmpty() || file.getSize()>maxBytes) return ResponseEntity.badRequest().body(Map.of("error","Empty file or file too large"));
        String detected=detectType(file);
        if (detected==null) return ResponseEntity.badRequest().body(Map.of("error","Only PDF, JPEG and PNG are accepted"));
        UUID uid=UUID.fromString(auth.getName());
        String key=UUID.randomUUID().toString();
        Path target=storage.resolve(key).normalize();
        if (!target.startsWith(storage)) throw new SecurityException("Invalid storage path");
        try (InputStream in=file.getInputStream()) { Files.copy(in,target); }
        String original=Optional.ofNullable(file.getOriginalFilename()).orElse("medical-report").replaceAll("[^A-Za-z0-9._-]","_");
        var saved=repo.save(new MedicalReport(uid,original,original,key,detected,file.getSize()));
        return ResponseEntity.status(201).body(Map.of("id",saved.getId(),"fileName",original,"mimeType",detected));
    }

    @GetMapping
    public List<MedicalReport> list(Authentication auth) {
        return repo.findAllByUserIdOrderByCreatedAtDesc(UUID.fromString(auth.getName()));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> download(@PathVariable UUID id, Authentication auth) throws Exception {
        var report=repo.findByIdAndUserId(id,UUID.fromString(auth.getName()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Path file=storage.resolve(report.getStorageKey()).normalize();
        if (!file.startsWith(storage) || !Files.isRegularFile(file))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(report.getMimeType()))
            .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+report.getOriginalFileName()+"\"")
            .body(Files.readAllBytes(file));
    }

    private String detectType(MultipartFile f) throws Exception {
        byte[] b=new byte[8];
        try (InputStream in=f.getInputStream()) { int n=in.read(b); if(n<4)return null; }
        if (b[0]=='%' && b[1]=='P' && b[2]=='D' && b[3]=='F') return "application/pdf";
        if ((b[0]&255)==0xFF && (b[1]&255)==0xD8 && (b[2]&255)==0xFF) return "image/jpeg";
        if ((b[0]&255)==0x89 && b[1]=='P' && b[2]=='N' && b[3]=='G') return "image/png";
        return null;
    }
}
