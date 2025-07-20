package com.example.DMS.controllers;

import com.example.DMS.DTO.DocumentDTO;
import com.example.DMS.DTO.UpdateDocumentMetadataRequest;
import com.example.DMS.DTO.UploadDocumentRequest;
import com.example.DMS.services.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDTO> uploadDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart("meta") String metaJson) throws IOException {

        // Convert JSON string to UploadDocumentRequest
        UploadDocumentRequest meta = objectMapper.readValue(metaJson, UploadDocumentRequest.class);

        DocumentDTO uploaded = documentService.uploadDocument(file, meta);
        return ResponseEntity.ok(uploaded);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) throws FileNotFoundException {
        return documentService.downloadDocument(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable String id) {
        documentService.softDelete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<String> preview(@PathVariable String id) throws IOException {
        return documentService.previewDocument(id);
    }

    @GetMapping("/workspaces/{workspaceId}/documents")
    public ResponseEntity<List<DocumentDTO>> getByWorkspace(@PathVariable String workspaceId) {
        return documentService.getByWorkspace(workspaceId);
    }

    @GetMapping("/users/documents")
    public ResponseEntity<List<DocumentDTO>> getByUser() {
        return documentService.getByUser();
    }

    @GetMapping("/{id}/metadata")
    public ResponseEntity<DocumentDTO> getMetadata(@PathVariable String id) {
        return documentService.getMetadata(id);
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity<DocumentDTO> updateMetadata(@PathVariable String id, @RequestBody UpdateDocumentMetadataRequest update) {
        return documentService.updateMetadata(id, update);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DocumentDTO>> search(@RequestParam String keyword) {
        return documentService.search(keyword);
    }
}
