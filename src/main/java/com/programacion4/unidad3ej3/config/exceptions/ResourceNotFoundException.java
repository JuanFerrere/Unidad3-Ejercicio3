package com.programacion4.unidad3ej3.config.exceptions;

import org.springframework.http.HttpStatus;
import java.util.Collections;

public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, Collections.emptyList());
    }
}