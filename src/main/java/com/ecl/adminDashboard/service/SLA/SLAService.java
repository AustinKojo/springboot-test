package com.ecl.adminDashboard.service.SLA;

import com.ecl.adminDashboard.dto.*;
import com.ecl.adminDashboard.model.SLA;
import org.springframework.web.multipart.MultipartFile;

public interface SLAService {

    public ResponseObjectSLA createSLA(SlaRequestDTO sla);

    public ResponseObjectListSLA getSLA(String companyName);

//    public ResponseObjectProduct getProductById (long id);

    public ResponseObjectSLA updateSLA (long id, SLA updateProduct);

    public ResponseObjectSLA deleteSLA(long id);

    public ResponseObjectSLA uploadSlasFromExcel(MultipartFile file);

    byte[] exportSlasToExcel();
}
