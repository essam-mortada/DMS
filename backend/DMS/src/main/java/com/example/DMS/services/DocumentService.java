package com.example.DMS.services;

import com.example.DMS.DTO.DocumentDTO;
import com.example.DMS.DTO.UpdateDocumentMetadataRequest;
import com.example.DMS.DTO.UploadDocumentRequest;
import com.example.DMS.config.JwtUtils;
import com.example.DMS.models.DmsDocument;
import com.example.DMS.repository.DocumentRepository;
import com.example.DMS.mappers.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    private final DocumentMapper documentMapper;

    public DocumentDTO uploadDocument(MultipartFile file, UploadDocumentRequest meta) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        final long fileSize = meta.getSizeOrDefault(file.getSize());
        if (fileSize <= 0) {
            throw new IllegalArgumentException("File size must be positive");
        }

        String userNid = currentUserService.getCurrentUserNid();
        String storedFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get("uploads").resolve(storedFileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        DmsDocument document = DmsDocument.builder()
                .name(StringUtils.hasText(meta.getName()) ? meta.getName() : file.getOriginalFilename())
                .size(fileSize)
                .type(StringUtils.hasText(meta.getType()) ? meta.getType() : file.getContentType())
                .workspaceId(meta.getWorkspaceId())
                .folderId(meta.getFolderId())
                .ownerNid(userNid)
                .url(filePath.toString())
                .deleted(false)
                .build();

        return documentMapper.toDto(documentRepository.save(document));
    }

    public Resource downloadDocument(String id) throws FileNotFoundException {
        DmsDocument doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        if (doc.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found or deleted");
        }

        File file = new File(doc.getUrl());
        if (!file.exists()) {
            throw new FileNotFoundException("File not found on disk");
        }
        return new FileSystemResource(file);
    }

    public void softDelete(String id) {
        if(documentRepository.findByOwnerNid(currentUserService.getCurrentUserNid()).stream().noneMatch(ws -> ws.getId().equals(id))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this document");
        }
        DmsDocument doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        doc.setDeleted(true);
        documentRepository.save(doc);
    }

    public String previewDocument(String id) throws IOException {
        if(documentRepository.findByOwnerNid(currentUserService.getCurrentUserNid()).stream().noneMatch(ws -> ws.getId().equals(id))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to view this document");
        }
        DmsDocument doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        File file = new File(doc.getUrl());
        byte[] bytes = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(bytes);
    }

    public List<DocumentDTO> getDocumentsByWorkspace(String workspaceId) {
        return documentRepository.findByWorkspaceIdAndDeletedFalse(workspaceId)
                .stream()
                .map(documentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<DocumentDTO> getDocumentsByOwner() {
        String nid = currentUserService.getCurrentUserNid();
        return documentRepository.findByOwnerNidAndDeletedFalse(nid)
                .stream()
                .map(documentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<DocumentDTO> getDocumentsByFolder(String folderId) {
        return documentRepository.findByFolderIdAndDeletedFalse(folderId)
                .stream()
                .map(documentMapper::toDto)
                .collect(Collectors.toList());
    }

    public DocumentDTO getDocumentMetadata(String id) {
        DmsDocument doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
        return documentMapper.toDto(doc);
    }

    public DocumentDTO updateDocumentMetadata(String id, UpdateDocumentMetadataRequest request) {
        if(documentRepository.findByOwnerNid(currentUserService.getCurrentUserNid()).stream().noneMatch(ws -> ws.getId().equals(id))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update this document");
        }
        DmsDocument doc = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        doc.setName(request.getName());
        doc.setTags(request.getTags());

        return documentMapper.toDto(documentRepository.save(doc));
    }

    public List<DocumentDTO> searchDocuments(String keyword) {
        return documentRepository.findByNameContainingIgnoreCaseAndDeletedFalse(keyword)
                .stream()
                .map(documentMapper::toDto)
                .collect(Collectors.toList());
    }
}