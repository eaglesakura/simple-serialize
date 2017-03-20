package com.eaglesakura.serialize.error;

public class SerializeIdConflictException extends SerializeException {
    public SerializeIdConflictException() {
    }

    public SerializeIdConflictException(String message) {
        super(message);
    }

    public SerializeIdConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializeIdConflictException(Throwable cause) {
        super(cause);
    }
}
