package com.programacion4.unidad3ej3.config.exceptions;

import org.springframework.http.HttpStatus;
import java.util.Collections;

public class ConflictException extends CustomException {
    public ConflictException(String message) {
        // Asumiendo que tu CustomException recibe (mensaje, estado HTTP, lista de errores)
        // Si el constructor es distinto, ajústalo a lo que pida tu CustomException
        super(message, HttpStatus.CONFLICT, Collections.emptyList());
    }
}