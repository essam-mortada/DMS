package com.example.DMS.DTO;

import lombok.Data;
import java.util.List;

@Data
public class UpdateDocumentMetadataRequest {
    private String name;
    private List<String> tags;
}