package com.example.telegrambotdemo.controller;

import com.example.telegrambotdemo.receiver.ElementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ElementController {

   private final ElementService elementService;

   @GetMapping("/download")
   public ResponseEntity<InputStreamResource> download() throws IOException {
       String fileName = "element_tree.xlsx";
       ByteArrayInputStream inputStream = elementService.getExcel();
       InputStreamResource resource = new InputStreamResource(inputStream);
       ResponseEntity<InputStreamResource> response = ResponseEntity.ok()
               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+fileName)
               .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
               .body(resource);
       return response;
   }
}
