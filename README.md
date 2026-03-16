# Spring AI Backend (Simple Student Help Demo)

This is a **simple backend project** made with Spring Boot + Spring AI.
Topic is easy: **student handbook Q&A**.

## Spring Initializr-style folder structure
```text
llm-backend/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/example/llmbackend/
    │   │   ├── LlmBackendApplication.java
    │   │   ├── config/AiConfig.java
    │   │   ├── controller/RagController.java
    │   │   ├── model/
    │   │   └── service/
    │   └── resources/
    │       ├── application.yml
    │       └── docs/student-handbook.txt
    └── test/java/com/example/llmbackend/
```

## What it does (simple)
1. Load a sample student handbook document (or upload your own text file).
2. Store document chunks in vector store.
3. Ask questions in message format.
4. Get answer from Spring AI with RAG.

## APIs
- `GET /api/rag/health`
- `POST /api/rag/documents/load-student-sample`
- `POST /api/rag/documents/upload` (multipart file)
- `POST /api/rag/messages` (JSON)

## Quick start
### 1) Set API key
```bash
export OPENAI_API_KEY=your_key_here
```

### 2) Run app
```bash
mvn spring-boot:run
```

### 3) Load sample student document
```bash
curl -X POST http://localhost:8080/api/rag/documents/load-student-sample
```

### 4) Ask simple question
```bash
curl -X POST http://localhost:8080/api/rag/messages \
  -H "Content-Type: application/json" \
  -d '{"message":"What is library timing?"}'
```
