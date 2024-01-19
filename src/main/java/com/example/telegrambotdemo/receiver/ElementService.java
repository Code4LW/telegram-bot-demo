package com.example.telegrambotdemo.receiver;

import com.example.telegrambotdemo.model.Element;
import com.example.telegrambotdemo.repository.ElementRepo;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    public boolean addElement(String parentName, String childName){
        if(elementRepo.findByName(parentName).isEmpty()){
            LOGGER.error("The given parent element is not found!");
            return false;
        }
        if(elementRepo.findByName(childName).isEmpty()) {
            Element parent = elementRepo.findByName(parentName).get();
            Element child = new Element();
            child.setName(childName);
            child.setParent(parent.getName());
            child.setLevel(parent.getLevel() + 1);
            elementRepo.save(child);
            return true;
        }
        return false;
    }
    public boolean existByName(String name){
        if(elementRepo.findByName(name).isEmpty()) return false;
        return true;
    }
    public boolean deleteElement(Element element){
        if(elementRepo.findById(element.getId()).isEmpty()) return false;
         elementRepo.deleteById(element.getId());
         List<Element> children = elementRepo.findAll().stream().filter(el -> el.hasParent()).filter(element1 -> element1.getParent().equals(element.getName())).toList();
         children.forEach(this::deleteElement);
         return true;
    }
    public byte[] getExcel() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        List<Element> elements = elementRepo.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        try {
        HSSFSheet sheet = workbook.createSheet("Category Tree");
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


    public void getFromExcel(HSSFWorkbook workbook) {
        HSSFSheet sheet = workbook.getSheetAt(0);
        elementRepo.deleteAll();
        int rowIndex = 1;
        while(sheet.getRow(rowIndex)!=null){
            HSSFRow row = sheet.getRow(rowIndex);
            Long id = (long) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();
            String parent = row.getCell(2).getStringCellValue();
            int level = (int) row.getCell(3).getNumericCellValue();
            elementRepo.save(new Element(id, name, parent, level));
            rowIndex++;
        }
    }
    //Реализация метода для построения схемы дерева из отсортированного списка
    public String drawTree(){
        List<Element> elements = elementRepo.findAll();
        //to build the string of the tree
        StringBuilder stringBuilder = new StringBuilder();
        elements = sortList(elements);

        //to keep track of levels of each element on the tree
        List<Integer> levels = new ArrayList<>();
        List<Integer> lvlsCopy= new ArrayList<>();
        int lvl;
        int index;

        //to store the created string branches
        List<String> branches = new ArrayList<>();
        String branch;

        //iterating through the sorted list
        for(int j = 0; j < elements.size(); j++){
            branch = elements.get(j).getName();
            if(!levels.contains(elements.get(j).getLevel())) {
                levels.add(elements.get(j).getLevel());
                lvlsCopy.add(elements.get(j).getLevel());
            }else{
                int times = levels.size()-1-levels.indexOf(elements.get(j).getLevel());
                String sample = branches.get(lvlsCopy.indexOf(elements.get(j).getLevel()));
                for(int k = 0; k < times; k++){
                    String replacable = branches.get(branches.size()-1-k);
                    int temp = stringBuilder.indexOf(replacable);
                    index = temp + sample.indexOf('└');
                    stringBuilder.replace(index, index+1, "│");
                    branches.set(branches.size()-1-k,stringBuilder.substring(temp,(temp+replacable.length())));
                }
                final int copylv = elements.get(j).getLevel();
                lvlsCopy = lvlsCopy.stream().filter(l -> l < copylv).collect(Collectors.toList());
                lvlsCopy.add(elements.get(j).getLevel());
                levels = levels.stream().map(lv -> lv>=copylv?lv*(-2):lv).collect(Collectors.toList());
                levels.add(elements.get(j).getLevel());
            }
            //concatinating the branch symbols to create tree-like design
            if(elements.get(j).getLevel()>1) {
                if(j== elements.size()-1||elements.get(j).getLevel()!=elements.get(j+1).getLevel()){
                    branch = "└─["+branch;
                }else branch = "├─["+branch;
            }
            // setting the depth of each created branch based on the level its element
            lvl = elements.get(j).getLevel()-1;
            int adlv = lvl == 0? 0: elements.get(j).getLevel();
            for (int i = 1; i < (lvl * 3) - 1 + adlv; i++) {
                branch = "  " + branch;
            }
            branches.add(branch);
            stringBuilder.append(branch).append("\n");
        }
        return stringBuilder.toString();
    }

    //Реализация сортировки списка элементов для построения дtрева
    public List<Element> sortList(List<Element> elements) {
        //Создаем LinkedList объект так как туда будет удобнее добавлять новые элементы в список
        List<Element> sorted = new LinkedList<>();
        List<Element> childElements;
        Set<Element> set = new HashSet<>();
        for (int i = 0; i < elements.size(); i++) {
            if (set.add(elements.get(i))) {
                sorted.add(elements.get(i));
            }
            final Element current = elements.get(i);

            // ищем все дочерние элементы данного элемента
            childElements = elements.stream().filter(Element::hasParent).filter(element -> element.getParent().equals(current.getName())).collect(Collectors.toList());
            if(childElements.isEmpty()) continue;

            //При наличии дочерних элементов добавляем кажыдй из них в отсортированный список после родительского элемента
            int index = sorted.indexOf(elements.get(i));

            for (Element child : childElements) {
                index++;
                sorted.add(index, child);
                set.add(child);
            }

        }
        return sorted;
    }
}
