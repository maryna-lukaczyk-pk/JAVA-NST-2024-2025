package com.example.projectmanagerapp.exception; // UPEWNIJ SIĘ, ŻE TA LINIA JEST POPRAWNA

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}