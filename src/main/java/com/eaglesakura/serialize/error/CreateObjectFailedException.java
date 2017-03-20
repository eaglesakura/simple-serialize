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

}
