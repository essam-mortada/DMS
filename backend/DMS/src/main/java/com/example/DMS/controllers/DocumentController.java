package com.example.DMS.controllers;

import com.example.DMS.DTO.DocumentDTO;
import com.example.DMS.DTO.UpdateDocumentMetadataRequest;
import com.example.DMS.DTO.UploadDocumentRequest;
import com.example.DMS.services.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        UploadDocumentRequest meta = objectMapper.readValue(metaJson, UploadDocumentRequest.class);
        return ResponseEntity.ok(documentService.uploadDocument(file, meta));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) throws IOException {
        Resource resource = documentService.downloadDocument(id);
        DocumentDTO metadata = documentService.getDocumentMetadata(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + metadata.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable String id) {
        documentService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<String> preview(@PathVariable String id) throws IOException {
        return ResponseEntity.ok(documentService.previewDocument(id));
    }

    @GetMapping("/workspaces/{workspaceId}/documents")
    public ResponseEntity<List<DocumentDTO>> getByWorkspace(@PathVariable String workspaceId) {
        return ResponseEntity.ok(documentService.getDocumentsByWorkspace(workspaceId));
    }

    @GetMapping("/users/documents")
    public ResponseEntity<List<DocumentDTO>> getByUser() {
        return ResponseEntity.ok(documentService.getDocumentsByOwner());
    }

    @GetMapping("/{id}/metadata")
    public ResponseEntity<DocumentDTO> getMetadata(@PathVariable String id) {
        return ResponseEntity.ok(documentService.getDocumentMetadata(id));
    }

    @GetMapping("/folder/{id}/documents")
    public ResponseEntity<List<DocumentDTO>> getByFolder(@PathVariable String id) {
        return ResponseEntity.ok(documentService.getDocumentsByFolder(id));
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity<DocumentDTO> updateMetadata(
            @PathVariable String id,
            @RequestBody UpdateDocumentMetadataRequest update) {
        return ResponseEntity.ok(documentService.updateDocumentMetadata(id, update));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DocumentDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(documentService.searchDocuments(keyword));
    }

    @GetMapping("/workspace/{workspaceId}/search")
    public List<DocumentDTO> searchByWorkspace(
            @PathVariable String workspaceId,
            @RequestParam String keyword) {
        return documentService.searchInWorkspace(workspaceId, keyword);
    }

    @GetMapping("/folder/{folderId}/search")
    public List<DocumentDTO> searchByFolder(
            @PathVariable String folderId,
            @RequestParam String keyword) {
        return documentService.searchInFolder(folderId, keyword);
    }


    @GetMapping("/sort")
    public ResponseEntity<List<DocumentDTO>> sort(@RequestParam String workspaceId, @RequestParam String sort) {
        return ResponseEntity.ok(documentService.sortDocuments(workspaceId, sort));
    }

    @GetMapping("/user/sort")
    public ResponseEntity<List<DocumentDTO>> sortUser(@RequestParam String sort) {
        return ResponseEntity.ok(documentService.sortDocumentsByOwner(sort));
    }

    @GetMapping("/folder/{folderId}/sort")
    public ResponseEntity<List<DocumentDTO>> sortFolder(@PathVariable String folderId, @RequestParam String sort) {
        return ResponseEntity.ok(documentService.sortDocumentsByFolder(folderId, sort));
    }
}