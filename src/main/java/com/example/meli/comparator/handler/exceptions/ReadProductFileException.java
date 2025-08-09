package com.example.meli.comparator.handler.exceptions;

import java.io.IOException;

public class ReadProductFileException extends RuntimeException {
    public ReadProductFileException(String message, IOException e) {
        super(message, e);
    }
}
