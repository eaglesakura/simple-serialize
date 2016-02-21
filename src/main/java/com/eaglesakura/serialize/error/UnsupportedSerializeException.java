package com.eaglesakura.serialize.error;

public class UnsupportedSerializeException extends SerializeException {
    public UnsupportedSerializeException() {
    }

    public UnsupportedSerializeException(String message) {
        super(message);
    }

    public UnsupportedSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedSerializeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedSerializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
