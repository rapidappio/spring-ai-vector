package com.rapidapp.springaivector;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringAiVectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiVectorApplication.class, args);
    }

}

record CreateDocumentRequest(String text, Map<String, Object> meta) {}

@RestController
@RequestMapping("/documents")
class DocumentController {

    @Autowired
    private VectorStore vectorStore;

    @PostMapping
    public void create(@RequestBody CreateDocumentRequest request) {
        vectorStore.add(List.of(new Document(request.text(), request.meta())));
    }

    @GetMapping
    public String list(@RequestParam("query") String query) {
        List<Document> results = vectorStore.similaritySearch(SearchRequest.query(query).withTopK(5));
        return results.toString();
    }
}
