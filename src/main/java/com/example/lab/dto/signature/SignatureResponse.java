package com.example.lab.dto.signature;

import com.example.lab.model.signature.SignatureStatus;

import java.time.Instant;
import java.util.UUID;

public class SignatureResponse {

    private UUID id;
    private String threatName;
    private String firstBytesHex;
    private String remainderHashHex;
    private long remainderLength;
    private String fileType;
    private long offsetStart;
    private long offsetEnd;
    private Instant updatedAt;
    private SignatureStatus status;
    private String digitalSignatureBase64;

    public SignatureResponse() {}

    public SignatureResponse(UUID id, String threatName, String firstBytesHex,
                             String remainderHashHex, long remainderLength,
                             String fileType, long offsetStart, long offsetEnd,
                             Instant updatedAt, SignatureStatus status,
                             String digitalSignatureBase64) {
        this.id = id;
        this.threatName = threatName;
        this.firstBytesHex = firstBytesHex;
        this.remainderHashHex = remainderHashHex;
        this.remainderLength = remainderLength;
        this.fileType = fileType;
        this.offsetStart = offsetStart;
        this.offsetEnd = offsetEnd;
        this.updatedAt = updatedAt;
        this.status = status;
        this.digitalSignatureBase64 = digitalSignatureBase64;
    }

    public UUID getId() { return id; }
    public String getThreatName() { return threatName; }
    public String getFirstBytesHex() { return firstBytesHex; }
    public String getRemainderHashHex() { return remainderHashHex; }
    public long getRemainderLength() { return remainderLength; }
    public String getFileType() { return fileType; }
    public long getOffsetStart() { return offsetStart; }
    public long getOffsetEnd() { return offsetEnd; }
    public Instant getUpdatedAt() { return updatedAt; }
    public SignatureStatus getStatus() { return status; }
    public String getDigitalSignatureBase64() { return digitalSignatureBase64; }
}