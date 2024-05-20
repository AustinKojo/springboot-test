package com.ecl.adminDashboard.service.Documents;

import com.ecl.adminDashboard.dto.*;
import com.ecl.adminDashboard.dto.DocumentRequestDTO;
import com.ecl.adminDashboard.dto.ResponseObjectDocument;
import com.ecl.adminDashboard.dto.ResponseObjectListDocument;

public interface DocumentsService {
    public ResponseObjectDocument createDocument(DocumentRequestDTO document);

    public ResponseObjectListDocument getDocument(String enrolmentId);

    public ResponseObjectListDocument getDocumentBySlaId (long slaId);

    public ResponseObjectDocument updateDocument (long id, DocumentRequestDTO updateDocumentDTO);

    public ResponseObjectDocument deleteDocument (long id);

}
