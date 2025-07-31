package com.example.DMS.controllers;

import com.example.DMS.models.DmsDocument;
import com.example.DMS.models.Folder;
import com.example.DMS.services.CustomUserDetails;
import com.example.DMS.repository.DocumentRepository;
import com.example.DMS.repository.FolderRepository;
import com.example.DMS.DTO.CreateShareLinkRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/share-link")
@RequiredArgsConstructor
public class ShareLinkController {

    private final DocumentRepository documentRepository;
    private final FolderRepository folderRepository;

    @PostMapping("/document/{documentId}")
    public ResponseEntity<?> createDocumentShareLink(@PathVariable String documentId, @RequestBody CreateShareLinkRequest request, Authentication authentication) {
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        DmsDocument document = documentRepository.findById(documentId).orElseThrow(() -> new com.example.DMS.exception.ResourceNotFoundException("Document not found"));

        if (!document.getOwnerNid().equals(currentUser.getNid())) {
            return ResponseEntity.status(403).body("Only the owner can create a share link.");
        }

        document.setLinkSharingEnabled(true);
        document.setShareLinkPermission(request.getPermission());
        if (document.getShareLinkToken() == null) {
            document.setShareLinkToken(UUID.randomUUID().toString());
        }
        documentRepository.save(document);
        return ResponseEntity.ok(document.getShareLinkToken());
    }

    @DeleteMapping("/document/{documentId}")
    public ResponseEntity<?> deleteDocumentShareLink(@PathVariable String documentId, Authentication authentication) {
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        DmsDocument document = documentRepository.findById(documentId).orElseThrow(() -> new com.example.DMS.exception.ResourceNotFoundException("Document not found"));

        if (!document.getOwnerNid().equals(currentUser.getNid())) {
            return ResponseEntity.status(403).body("Only the owner can delete a share link.");
        }

        document.setLinkSharingEnabled(false);
        documentRepository.save(document);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/folder/{folderId}")
    public ResponseEntity<?> createFolderShareLink(@PathVariable String folderId, @RequestBody CreateShareLinkRequest request, Authentication authentication) {
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new com.example.DMS.exception.ResourceNotFoundException("Folder not found"));

        if (!folder.getCreatedBy().equals(currentUser.getNid())) {
            return ResponseEntity.status(403).body("Only the owner can create a share link.");
        }

        folder.setLinkSharingEnabled(true);
        folder.setShareLinkPermission(request.getPermission());
        if (folder.getShareLinkToken() == null) {
            folder.setShareLinkToken(UUID.randomUUID().toString());
        }
        folderRepository.save(folder);
        return ResponseEntity.ok(folder.getShareLinkToken());
    }

    @DeleteMapping("/folder/{folderId}")
    public ResponseEntity<?> deleteFolderShareLink(@PathVariable String folderId, Authentication authentication) {
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new com.example.DMS.exception.ResourceNotFoundException("Folder not found"));

        if (!folder.getCreatedBy().equals(currentUser.getNid())) {
            return ResponseEntity.status(403).body("Only the owner can delete a share link.");
        }

        folder.setLinkSharingEnabled(false);
        folderRepository.save(folder);
        return ResponseEntity.noContent().build();
    }
}
