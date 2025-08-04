package com.example.DMS.services;

import com.example.DMS.repository.DocumentRepository;
import com.example.DMS.models.DmsDocument;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class HuggingFaceService {

    private static final String HUGGINGFACE_API_URL = "https://api-inference.huggingface.co/models/facebook/bart-large-cnn";
    private static final int MAX_CHARS = 1500;

    @Value("${huggingface.api.key}")
    private String huggingFaceApiKey;

    private final ObjectMapper objectMapper;
    private final DocumentTextExtractor documentTextExtractor;
    private final DocumentRepository documentRepository;

    private CloseableHttpClient httpClient;

    @PostConstruct
    public void init() {
        this.httpClient = HttpClients.createDefault();
    }

    public String summarize(String text) throws IOException {
        if (text.length() > MAX_CHARS) {
            text = text.substring(0, MAX_CHARS);
        }

        HttpPost request = new HttpPost(HUGGINGFACE_API_URL);
        request.setHeader("Authorization", "Bearer " + huggingFaceApiKey);
        request.setHeader("Content-Type", "application/json");

        String payload = objectMapper.writeValueAsString(Map.of("inputs", text));
        request.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseBody = EntityUtils.toString(response.getEntity());

            if (response.getStatusLine().getStatusCode() >= 400) {
                throw new RuntimeException("Error from Hugging Face: " + responseBody);
            }

            JsonNode result = objectMapper.readTree(responseBody);
            return result.get(0).get("summary_text").asText();
        }
    }

    public String summarizeDocumentById(String docId) throws Exception {
        DmsDocument doc = documentRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found: " + docId));

        File file = new File(doc.getUrl());
        String content = documentTextExtractor.extractText(file);

        return this.summarize(content);
    }
}
