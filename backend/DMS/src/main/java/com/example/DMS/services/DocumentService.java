package com.example.DMS.services;

import com.example.DMS.DTO.DocumentDTO;
import com.example.DMS.DTO.UpdateDocumentMetadataRequest;
import com.example.DMS.DTO.UploadDocumentRequest;
import com.example.DMS.config.JwtUtils;
import com.example.DMS.models.DmsDocument;
import com.example.DMS.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final JwtUtils currentUserService;

    public DocumentDTO uploadDocument(MultipartFile file, UploadDocumentRequest meta) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        String userNid = currentUserService.getCurrentUserNid();

        String storedFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get("uploads").resolve(storedFileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        DmsDocument document = DmsDocument.builder()
                .name(meta.getName() != null ? meta.getName() : file.getOriginalFilename())
                .type(meta.getType() != null ? meta.getType() : file.getContentType())
                .workspaceId(meta.getWorkspaceId())
                .folderId(meta.getFolderId())
                .ownerNid(userNid)
                .url(filePath.toString())
                .deleted(false)
                .build();

        DmsDocument saved = documentRepository.save(document);
        return toDto(saved);
    }


    public DocumentDTO saveDocumentMetadata(DmsDocument document) {
        DmsDocument saved = documentRepository.save(document);
        return toDto(saved);
    }

    public Optional<DmsDocument> getDocumentById(String id) {
        return documentRepository.findById(id);
    }

    public void softDeleteDocument(String id) {
        documentRepository.findById(id).ifPresent(doc -> {
            doc.setDeleted(true);
            documentRepository.save(doc);
        });
    }

    public List<DocumentDTO> getDocumentsByWorkspace(String workspaceId) {
        return documentRepository.findByWorkspaceIdAndDeletedFalse(workspaceId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<DocumentDTO> getDocumentsByOwner() {
        String ownerNid = currentUserService.getCurrentUserNid();
        return documentRepository.findByOwnerNidAndDeletedFalse(ownerNid)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<Resource> downloadDocument(String id) throws FileNotFoundException {
        Optional<DmsDocument> documentOpt = documentRepository.findById(id);
        if (documentOpt.isEmpty() || documentOpt.get().isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found or deleted");
        }

        DmsDocument doc = documentOpt.get();
        File file = new File(doc.getUrl());
        if (!file.exists()) throw new FileNotFoundException("File not found on disk");

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    public void softDelete(String id) {
        DmsDocument doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        doc.setDeleted(true);
        documentRepository.save(doc);
    }

    public ResponseEntity<String> previewDocument(String id) {
        DmsDocument doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        try {
            File file = new File(doc.getUrl());
            byte[] bytes = Files.readAllBytes(file.toPath());
            String base64 = Base64.getEncoder().encodeToString(bytes);
            return ResponseEntity.ok(base64);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading file", e);
        }
    }

    public ResponseEntity<List<DocumentDTO>> getByWorkspace(String workspaceId) {
        List<DmsDocument> docs = documentRepository.findByWorkspaceIdAndDeletedFalse(workspaceId);
        List<DocumentDTO> dtos = docs.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<List<DocumentDTO>> getByUser() {
        String nid = currentUserService.getCurrentUserNid();
        List<DmsDocument> docs = documentRepository.findByOwnerNidAndDeletedFalse(nid);
        List<DocumentDTO> dtos = docs.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<DocumentDTO> getMetadata(String id) {
        DmsDocument doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return ResponseEntity.ok(toDto(doc));
    }

    public ResponseEntity<DocumentDTO> updateMetadata(String id, UpdateDocumentMetadataRequest request) {
        DmsDocument doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        doc.setName(request.getName());
        doc.setTags(request.getTags());

        return ResponseEntity.ok(toDto(documentRepository.save(doc)));
    }

    public ResponseEntity<List<DocumentDTO>> search(String keyword) {
        List<DmsDocument> docs = documentRepository.findByNameContainingIgnoreCaseAndDeletedFalse(keyword);
        List<DocumentDTO> dtos = docs.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private DocumentDTO toDto(DmsDocument doc) {
        DocumentDTO dto = new DocumentDTO();
        dto.setId(doc.getId());
        dto.setName(doc.getName());
        dto.setType(doc.getType());
        dto.setOwnerNid(doc.getOwnerNid());
        dto.setWorkspaceId(doc.getWorkspaceId());
        dto.setFolderId(doc.getFolderId());
        dto.setTags(doc.getTags());
        dto.setDeleted(doc.isDeleted());
        dto.setCreatedAt(doc.getCreatedAt());
        return dto;
    }
}
