package com.ecl.adminDashboard.controller;


import com.ecl.adminDashboard.dto.*;
import com.ecl.adminDashboard.service.Documents.DocumentsService;
import com.ecl.adminDashboard.service.Enrolments.EnrolmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("eclDashboard/api/v1")
@AllArgsConstructor
public class DocumentController {

    private DocumentsService documentsService;

    @PostMapping(value = "/documents", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObjectDocument createDocument(@RequestBody DocumentRequestDTO documentDTO) {
        return documentsService.createDocument(documentDTO);
    }

    @GetMapping(value = "/documents")
    public ResponseObjectListDocument getDocument (@RequestParam(required = false) String documentName) {
        return documentsService.getDocument(documentName);
    }

    @GetMapping(value = "/documents/{slaId}")
    public ResponseObjectListDocument getDocumentBySlaId (@PathVariable("slaId") long slaId) {
        return documentsService.getDocumentBySlaId(slaId);
    }

    @PutMapping(value = "/documents/{id}")
    public ResponseObjectDocument updateDocument(@PathVariable("id") long id, @RequestBody DocumentRequestDTO documentDTO){
        return documentsService.updateDocument(id, documentDTO);
    }
    @DeleteMapping("/documents/{id}")
    public ResponseObjectDocument deleteDocument(@PathVariable("id") long id){
        return documentsService.deleteDocument(id);
    }
}
