package com.example.llmbackend;

import com.example.llmbackend.service.RagChatService;
import com.example.llmbackend.service.RagIngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.llmbackend.controller.RagController;
import com.example.llmbackend.model.ChatResponse;

@WebMvcTest(RagController.class)
class RagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RagIngestionService ragIngestionService;

    @MockBean
    private RagChatService ragChatService;

    @Test
    void healthEndpointShouldWork() throws Exception {
        mockMvc.perform(get("/api/rag/health"))
                .andExpect(status().isOk());
    }

    @Test
    void loadStudentSampleShouldReturnStoredChunks() throws Exception {
        when(ragIngestionService.loadStudentHandbookSample()).thenReturn(3);

        mockMvc.perform(post("/api/rag/documents/load-student-sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value("student-handbook.txt"))
                .andExpect(jsonPath("$.chunksStored").value(3));
    }

    @Test
    void uploadEndpointShouldStoreChunks() throws Exception {
        when(ragIngestionService.upload(any())).thenReturn(2);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "notes.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Simple test content".getBytes()
        );

        mockMvc.perform(multipart("/api/rag/documents/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value("notes.txt"))
                .andExpect(jsonPath("$.chunksStored").value(2));
    }

    @Test
    void messageEndpointShouldReturnAnswer() throws Exception {
        when(ragChatService.ask(anyString())).thenReturn(new ChatResponse("Library timing is 9 AM to 5 PM."));

        mockMvc.perform(post("/api/rag/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"What is library timing?\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Library timing is 9 AM to 5 PM."));
    }
}
