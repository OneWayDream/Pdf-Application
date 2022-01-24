package ru.itis.pdfserver.exceptions;

public class BlockedAccountException extends ResponseException {

    public BlockedAccountException() {
        super();
    }

    public BlockedAccountException(String message) {
        super(message);
    }

    public BlockedAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockedAccountException(Throwable cause) {
        super(cause);
    }

    protected BlockedAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
