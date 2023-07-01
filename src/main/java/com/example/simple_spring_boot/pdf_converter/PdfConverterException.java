package com.example.simple_spring_boot.pdf_converter;

public class PdfConverterException extends RuntimeException {
    public PdfConverterException(String message) {
        super(message);
    }
    public PdfConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}
