package com.rabbyte.librarymanagementsystem.exceptions;

public class QuotaExceededException extends LibraryException {
    public QuotaExceededException(String message) {
        super(message);
    }
}
