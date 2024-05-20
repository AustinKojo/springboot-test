package com.ecl.adminDashboard.controller;

import com.ecl.adminDashboard.dto.*;
import com.ecl.adminDashboard.model.SLA;
import com.ecl.adminDashboard.service.SLA.SLAService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("eclDashboard/api/v1")
@AllArgsConstructor
public class SLAController {
    private SLAService slaService;

    @PostMapping(value = "/sla", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseObjectSLA createSLA (@RequestBody SlaRequestDTO slaDTO){

        return slaService.createSLA(slaDTO);
    }

    @GetMapping(value = "/sla")
    public ResponseObjectListSLA getSLA (@RequestParam(required = false) String companyName){
        return slaService.getSLA(companyName);
    }

    @PutMapping(value = "/sla/{id}")
    public ResponseObjectSLA updateSLA(@PathVariable("id") long id, @RequestBody SLA sla){
        return slaService.updateSLA(id, sla);
    }

    @DeleteMapping(value = "/sla/{id}")
    public ResponseObjectSLA deleteSLA(@PathVariable("id") long id){
        return slaService.deleteSLA(id);
    }

    @PostMapping("/sla/upload")
    public ResponseEntity<ResponseObjectSLA> uploadSlasFromExcel(@RequestParam("file") MultipartFile file) {
        ResponseObjectSLA response = slaService.uploadSlasFromExcel(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sla/download/excel")
    public ResponseEntity<byte[]> downloadSlasAsExcel() {
        byte[] excelBytes = slaService.exportSlasToExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "slas.xlsx");
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }


}


