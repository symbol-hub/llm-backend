package com.example.llmbackend.service;

import com.example.llmbackend.model.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class RagChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagChatService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    public ChatResponse ask(String message) {
        String answer = chatClient.prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().topK(4).build()))
                .user(message)
                .call()
                .content();

        return new ChatResponse(answer);
    }
}
