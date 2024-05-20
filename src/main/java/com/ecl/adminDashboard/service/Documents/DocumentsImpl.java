package com.ecl.adminDashboard.service.Documents;


import com.ecl.adminDashboard.dto.*;
import com.ecl.adminDashboard.model.Customer;
import com.ecl.adminDashboard.model.Documents;
import com.ecl.adminDashboard.model.Enrolment;
import com.ecl.adminDashboard.model.SLA;

import com.ecl.adminDashboard.model.Product;
import com.ecl.adminDashboard.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DocumentsImpl implements DocumentsService{

    private EnrolmentRepository enrolmentRepository;
    private SLARepository slaRepository;
    private DocumentRepository documentRepository;

    @Override
    public ResponseObjectDocument createDocument(DocumentRequestDTO documentDTO) {
        ResponseObjectDocument resp = new ResponseObjectDocument();
        Documents document = new Documents();

        // Fetch Customer and Product entities based on the provided IDs
//        Enrolment enrolment = enrolmentRepository.findById(documentDTO.getEnrolmentId()).orElse(null);
          SLA sla = slaRepository.findById(documentDTO.getSlaId()).orElse(null);

        // Check if both Customer and Product are found
        if (sla != null) {
            document.setSla(sla);

            document.setDocumentType(documentDTO.getDocumentType());
            document.setDocumentName(documentDTO.getDocumentName());

            Documents newDocument = documentRepository.save(document);


            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(newDocument);
        } else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("SLa not found");
        }

        return resp;
    }




    @Override
    public ResponseObjectListDocument getDocument(String documentName){
        ResponseObjectListDocument resp = new ResponseObjectListDocument();

        try {
            List<Documents> documents = documentRepository.findAllByOrderByCreatedOnDesc();

            if (!documents.isEmpty()){
                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject(documents);
            }
            else {
                resp.setResponseCode("E01");
                resp.setResponseMessage("Error");
            }
        }
        catch (Exception e){
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to retrieve sla: " + e.getMessage());
        }
        return  resp;
    }

    @Override
    public ResponseObjectListDocument getDocumentBySlaId(long slaId) {
        ResponseObjectListDocument resp = new ResponseObjectListDocument();

        try {
            // Check if enrolment with given ID exists
            Optional<SLA> slaOptional = slaRepository.findById(slaId);
            if (slaOptional.isPresent()) {
                SLA sla = slaOptional.get();

                // Retrieve all documents tied to the enrolment
                List<Documents> documents = documentRepository.findBySla(sla);

                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject(documents);
            } else {
                resp.setResponseCode("E01");
                resp.setResponseMessage("Sla not found for id: " + slaId);
            }
        } catch (Exception e) {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to retrieve documents: " + e.getMessage());
        }
        return resp;
    }

    @Override
    public ResponseObjectDocument updateDocument(long id, DocumentRequestDTO updateDocumentDTO) {
        ResponseObjectDocument resp = new ResponseObjectDocument();

        try {
            Optional<Documents> existingDocumentOptional = documentRepository.findById(id);

            if (existingDocumentOptional.isPresent()) {
                Documents existingDocument = existingDocumentOptional.get();

                // Update fields based on the EnrolmentRequestDTO
                existingDocument.setSla(slaRepository.findById(updateDocumentDTO.getSlaId()).orElse(null));
                existingDocument.setDocumentType(updateDocumentDTO.getDocumentType());
                existingDocument.setDocumentName(updateDocumentDTO.getDocumentName());

                Documents updatedDocumentEntity = documentRepository.save(existingDocument);

                resp.setResponseCode("000");
                resp.setResponseMessage("Success");
                resp.setObject(updatedDocumentEntity);
            } else {
                resp.setResponseCode("E01");
                resp.setResponseMessage("Enrolment not found for id: " + id);
            }
        } catch (Exception e) {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Failed to update enrolment: " + e.getMessage());
        }

        return resp;
    }


    @Override
    public ResponseObjectDocument deleteDocument(long id) {
        ResponseObjectDocument resp = new ResponseObjectDocument();
        Optional<Documents> existingDocument = documentRepository.findById(id);

        if (existingDocument.isPresent()){
            documentRepository.deleteById(existingDocument.get().getId());
            System.out.println("id deleted: " + existingDocument.get().getId());
            resp.setResponseCode("000");
            resp.setResponseMessage("Success");
            resp.setObject(existingDocument.get());
        }
        else {
            resp.setResponseCode("E01");
            resp.setResponseMessage("Product not found for id: " + id);
        }
        return resp;
    }

}
