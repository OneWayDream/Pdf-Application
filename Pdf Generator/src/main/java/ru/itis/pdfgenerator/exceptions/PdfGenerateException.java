package ru.itis.pdfgenerator.exceptions;

public class PdfGenerateException extends RuntimeException{

    public PdfGenerateException() {
        super();
    }

    public PdfGenerateException(String message) {
        super(message);
    }

    public PdfGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public PdfGenerateException(Throwable cause) {
        super(cause);
    }

    protected PdfGenerateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
