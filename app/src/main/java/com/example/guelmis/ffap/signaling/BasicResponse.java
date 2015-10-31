package com.example.guelmis.ffap.signaling;

public class BasicResponse {
    private boolean success;
    private String message;
    private String status;

    protected BasicResponse(boolean _success, String _message, String _status){
        success = _success;
        message = _message;
        status = _status;
    }

    public boolean success() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
