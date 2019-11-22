package com.ellen.supermessagelibrary;

public class MessageIdNullException extends RuntimeException {

    private String errorMessage;

    public MessageIdNullException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
