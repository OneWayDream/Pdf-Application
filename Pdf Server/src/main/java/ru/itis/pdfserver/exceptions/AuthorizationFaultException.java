package ru.itis.pdfserver.exceptions;

public class AuthorizationFaultException extends AuthorizationException {
    public AuthorizationFaultException() {
    }

    public AuthorizationFaultException(String message) {
        super(message);
    }

    public AuthorizationFaultException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationFaultException(Throwable cause) {
        super(cause);
    }

    protected AuthorizationFaultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
