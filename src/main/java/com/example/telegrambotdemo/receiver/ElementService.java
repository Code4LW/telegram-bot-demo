package com.example.telegrambotdemo.receiver;

import com.example.telegrambotdemo.model.Element;
import com.example.telegrambotdemo.repository.ElementRepo;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElementService.class);

    @Autowired
    private ElementRepo elementRepo;

    public List<Element> getAll(){
        return elementRepo.findAll();
    }
    public List<String> getNames(){
        List<String> names = new ArrayList<>();
        elementRepo.findAll().forEach(element -> names.add(element.getName()));
        return names;
    }
    public Element getByName (String name){
        return elementRepo.findByName(name).orElse(null);
    }
    public boolean addElement(Element element){
        if(elementRepo.findByName(element.getName()).isEmpty()) {
            if (!elementRepo.findAll().isEmpty()) {
                Element parent = elementRepo.findAll().get(0);
                element.setParent(parent.getName());
                element.setLevel(parent.getLevel()+1);
            }
            elementRepo.save(element);
            return true;
        }return false;
    }
    public void addElement(Element parent, Element child){
        if(elementRepo.findByName(parent.getName()).isEmpty()){
            LOGGER.error("The given parent element is not found!");
            return;
        }
        parent = elementRepo.findByName(parent.getName()).get();
        child.setParent(parent.getName());
        child.setLevel(parent.getLevel()+1);
        elementRepo.save(child);
    }
    public boolean existByName(String name){
        if(elementRepo.findByName(name).isEmpty()) return false;
        return true;
    }
    public boolean deleteElement(Element element){
        if(elementRepo.findByName(element.getName()).isEmpty()) return false;
         elementRepo.deleteById(element.getId());
         List<Element> children = elementRepo.findAll().stream().filter(el -> el.hasParent()).filter(element1 -> element1.getParent().equals(element.getName())).toList();
         children.forEach(element1 -> deleteElement(element1));
         return true;
    }
    public byte[] getExcel() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        List<Element> elements = elementRepo.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
        HSSFSheet sheet = workbook.createSheet(" Category Tree");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("name");
        row.createCell(2).setCellValue("parent");
        row.createCell(3).setCellValue("parent_element");
        int rowIndex = 1;
        for ( Element element: elements){
            HSSFRow newRow = sheet.createRow(rowIndex);
            newRow.createCell(0).setCellValue(element.getId());
            newRow.createCell(1).setCellValue(element.getName());
            newRow.createCell(2).setCellValue(element.getParent());
            newRow.createCell(3).setCellValue(element.getLevel());
            rowIndex++;
        }
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
       finally {
            workbook.close();
            byteArrayOutputStream.close();
        }
    }
}
