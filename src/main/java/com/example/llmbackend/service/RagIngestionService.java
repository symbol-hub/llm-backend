package com.example.llmbackend.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class RagIngestionService {

    private final VectorStore vectorStore;
    private final TokenTextSplitter tokenTextSplitter;

    public RagIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.tokenTextSplitter = new TokenTextSplitter();
    }

    public int upload(MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        return saveContent(content, file.getOriginalFilename());
    }

    public int loadStudentHandbookSample() throws IOException {
        ClassPathResource resource = new ClassPathResource("docs/student-handbook.txt");
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return saveContent(content, "student-handbook.txt");
    }

    private int saveContent(String content, String sourceName) {
        Document raw = new Document(content, Map.of("source", sourceName));
        List<Document> chunks = tokenTextSplitter.apply(List.of(raw));
        vectorStore.add(chunks);
        return chunks.size();
    }
}
