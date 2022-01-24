package ru.itis.pdfjwtserver.exceptions;

public class ExpiredJwtException extends AuthorizationException {
    public ExpiredJwtException() {
    }

    public ExpiredJwtException(String message) {
        super(message);
    }

    public ExpiredJwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredJwtException(Throwable cause) {
        super(cause);
    }
}
