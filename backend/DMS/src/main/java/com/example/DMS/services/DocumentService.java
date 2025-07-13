package com.example.DMS.services;

import com.example.DMS.models.DmsDocument;
import com.example.DMS.repository.DocumentRepository;
import com.example.DMS.repository.WorkspaceRepository;
import com.example.DMS.models.Workspace;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.DoubleStream;

public class DocumentService {

    @Component
    public class DummyDataLoader implements CommandLineRunner {
        private final WorkspaceRepository workspaceRepo;
        private final DocumentRepository documentRepo;
        private final MongoTemplate mongoTemplate;


        public DummyDataLoader(WorkspaceRepository workspaceRepo, DocumentRepository documentRepo, MongoTemplate mongoTemplate) {
            this.workspaceRepo = workspaceRepo;
            this.documentRepo = documentRepo;
            this.mongoTemplate = mongoTemplate;
        }

        @Override
        public void run(String... args) {
            System.out.println("DummyDataLoader executed!");

            // Ensure collections exist
            if (!mongoTemplate.collectionExists("workspaces")) {
                mongoTemplate.createCollection("workspaces");
            }
            if (!mongoTemplate.collectionExists("dms_documents")) {
                mongoTemplate.createCollection("dms_documents");
            }

            // Insert data
            Workspace workspace = Workspace.builder()
                    .name("Sample Workspace")
                    .userNid("1234567890")
                    .build();
            workspaceRepo.save(workspace);

            DmsDocument document = DmsDocument.builder()
                    .name("sample.pdf")
                    .type("pdf")
                    .url("/files/sample.pdf")
                    .ownerNid("1234567890")
                    .workspaceId(workspace.getId())
                    .build();
            documentRepo.save(document);
        }
    }

}
