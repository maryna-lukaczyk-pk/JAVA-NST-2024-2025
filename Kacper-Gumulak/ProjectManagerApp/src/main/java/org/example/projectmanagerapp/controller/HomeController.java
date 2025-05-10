package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Kontroler obsługujący endpoint startowy aplikacji
@RestController
@Tag(name = "Home", description = "Application home page")
public class HomeController {

    @GetMapping("/")
    @Operation(summary = "Home page", description = "Returns the application welcome message")
    public String home() { return "Welcome to the Project Manager App!"; }
}