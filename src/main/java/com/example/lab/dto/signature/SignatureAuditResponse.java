package com.example.lab.dto.signature;

import java.time.Instant;
import java.util.UUID;

public class SignatureAuditResponse {

    private Long auditId;
    private UUID signatureId;
    private String changedBy;
    private Instant changedAt;
    private String fieldsChanged;
    private String description;

    public SignatureAuditResponse() {}

    public SignatureAuditResponse(Long auditId, UUID signatureId,
                                  String changedBy, Instant changedAt,
                                  String fieldsChanged, String description) {
        this.auditId = auditId;
        this.signatureId = signatureId;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
        this.fieldsChanged = fieldsChanged;
        this.description = description;
    }

    public Long getAuditId() { return auditId; }
    public UUID getSignatureId() { return signatureId; }
    public String getChangedBy() { return changedBy; }
    public Instant getChangedAt() { return changedAt; }
    public String getFieldsChanged() { return fieldsChanged; }
    public String getDescription() { return description; }
}