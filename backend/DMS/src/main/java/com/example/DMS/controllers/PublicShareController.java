package com.example.DMS.controllers;

import com.example.DMS.exception.ResourceNotFoundException;
import com.example.DMS.mappers.DocumentMapper;
import com.example.DMS.mappers.FolderMapper;
import com.example.DMS.models.DmsDocument;
import com.example.DMS.repository.DocumentRepository;
import com.example.DMS.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/public/share")
@RequiredArgsConstructor
public class PublicShareController {

    private final DocumentRepository documentRepository;
    private final FolderRepository folderRepository;
    private final DocumentMapper documentMapper;
    private final FolderMapper folderMapper;

    @GetMapping("/{token}")
    public ResponseEntity<?> getSharedItem(@PathVariable String token) {
        return documentRepository.findByShareLinkTokenAndIsLinkSharingEnabledTrue(token)
                .<ResponseEntity<?>>map(document -> ResponseEntity.ok(documentMapper.toDto(document)))
                .orElseGet(() -> folderRepository.findByShareLinkTokenAndIsLinkSharingEnabledTrue(token)
                        .<ResponseEntity<?>>map(folder -> ResponseEntity.ok(folderMapper.toDto(folder)))
                        .orElse(ResponseEntity.notFound().build()));
    }

    @GetMapping("/preview/{token}")
    public ResponseEntity<Resource> getSharedDocumentPreview(@PathVariable String token) throws IOException {
        DmsDocument document = documentRepository.findByShareLinkTokenAndIsLinkSharingEnabledTrue(token)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found or sharing is disabled"));

        // This assumes document.getUrl() is a path to the file on your server
        // You might need to adjust this part based on how you store your files
        Resource resource = new UrlResource(Paths.get(document.getUrl()).toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getName() + "\"")
                .contentType(MediaType.parseMediaType(document.getType()))
                .body(resource);
    }

    @GetMapping("/download/{token}")
    public ResponseEntity<Resource> publicDownload(@PathVariable String token) throws IOException {
        DmsDocument document = documentRepository.findByShareLinkTokenAndIsLinkSharingEnabledTrue(token)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found or sharing is disabled"));

        Resource resource = new UrlResource(Paths.get(document.getUrl()).toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"") // Use "attachment" for downloads
                .contentType(MediaType.parseMediaType(document.getType()))
                .body(resource);
    }
}
