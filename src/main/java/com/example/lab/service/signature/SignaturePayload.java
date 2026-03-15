package com.example.lab.dto.signature;

import com.example.lab.model.signature.SignatureStatus;

public class SignaturePayload {

    private String threatName;
    private String firstBytesHex;
    private String remainderHashHex;
    private long remainderLength;
    private String fileType;
    private long offsetStart;
    private long offsetEnd;
    private SignatureStatus status;

    public SignaturePayload() {}

    public SignaturePayload(String threatName, String firstBytesHex,
                            String remainderHashHex, long remainderLength,
                            String fileType, long offsetStart, long offsetEnd,
                            SignatureStatus status) {
        this.threatName = threatName;
        this.firstBytesHex = firstBytesHex;
        this.remainderHashHex = remainderHashHex;
        this.remainderLength = remainderLength;
        this.fileType = fileType;
        this.offsetStart = offsetStart;
        this.offsetEnd = offsetEnd;
        this.status = status;
    }

    public String getThreatName() { return threatName; }
    public String getFirstBytesHex() { return firstBytesHex; }
    public String getRemainderHashHex() { return remainderHashHex; }
    public long getRemainderLength() { return remainderLength; }
    public String getFileType() { return fileType; }
    public long getOffsetStart() { return offsetStart; }
    public long getOffsetEnd() { return offsetEnd; }
    public SignatureStatus getStatus() { return status; }
}