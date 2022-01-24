package ru.itis.pdfserver.exceptions;

public class FaultResponseException extends ResponseException {
    public FaultResponseException() {
    }

    public FaultResponseException(String message) {
        super(message);
    }

    public FaultResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaultResponseException(Throwable cause) {
        super(cause);
    }

    protected FaultResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
