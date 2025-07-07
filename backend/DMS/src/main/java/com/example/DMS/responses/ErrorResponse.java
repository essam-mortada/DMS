package com.example.DMS.responses;

public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;
    private final long timestamp;

    public ErrorResponse(int status, String error, String message, long timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters
    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}