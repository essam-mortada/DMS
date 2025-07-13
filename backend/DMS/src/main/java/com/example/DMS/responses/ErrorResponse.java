package com.example.DMS.responses;

import java.util.List;

public class ErrorResponse {
    private final int status;
    private final String error;
    private List<String> messages;

    public ErrorResponse(int status, String error, List<String> messages, long timestamp) {
        this.status = status;
        this.error = error;
        this.messages = messages;
    }

    // Getters
    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public List<String> getMessage() {
        return messages;
    }

}