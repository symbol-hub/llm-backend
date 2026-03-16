package com.example.llmbackend.model;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(@NotBlank String message) {
}
