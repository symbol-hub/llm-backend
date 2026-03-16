package com.example.llmbackend.controller;

import com.example.llmbackend.model.ChatRequest;
import com.example.llmbackend.model.ChatResponse;
import com.example.llmbackend.model.UploadResponse;
import com.example.llmbackend.service.RagChatService;
import com.example.llmbackend.service.RagIngestionService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagIngestionService ragIngestionService;
    private final RagChatService ragChatService;

    public RagController(RagIngestionService ragIngestionService, RagChatService ragChatService) {
        this.ragIngestionService = ragIngestionService;
        this.ragChatService = ragChatService;
    }

    @GetMapping("/health")
    public String health() {
        return "Spring AI backend is running";
    }

    @PostMapping("/documents/load-student-sample")
    public UploadResponse loadStudentSample() throws IOException {
        int chunks = ragIngestionService.loadStudentHandbookSample();
        return new UploadResponse("student-handbook.txt", chunks);
    }

    @PostMapping(value = "/documents/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadResponse uploadDocument(@RequestPart("file") MultipartFile file) throws IOException {
        int chunks = ragIngestionService.upload(file);
        return new UploadResponse(file.getOriginalFilename(), chunks);
    }

    @PostMapping("/messages")
    public ChatResponse ask(@Valid @RequestBody ChatRequest request) {
        return ragChatService.ask(request.message());
    }
}
