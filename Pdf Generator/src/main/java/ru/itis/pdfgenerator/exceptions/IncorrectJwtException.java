package ru.itis.pdfgenerator.exceptions;

public class IncorrectJwtException extends AuthorizationException {
    public IncorrectJwtException() {
    }

    public IncorrectJwtException(String message) {
        super(message);
    }

    public IncorrectJwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectJwtException(Throwable cause) {
        super(cause);
    }
}
