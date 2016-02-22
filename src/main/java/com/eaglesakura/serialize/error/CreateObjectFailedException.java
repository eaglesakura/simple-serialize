package com.eaglesakura.serialize.error;

public class CreateObjectFailedException extends SerializeException {
    public CreateObjectFailedException() {
    }

    public CreateObjectFailedException(String message) {
        super(message);
    }

    public CreateObjectFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateObjectFailedException(Throwable cause) {
        super(cause);
    }

    public CreateObjectFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
