package com.example.telegrambotdemo.receiver;

import com.example.telegrambotdemo.model.Element;
import com.example.telegrambotdemo.repository.ElementRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void deleteElement(Element element){
         elementRepo.delete(element);
         elementRepo.deleteAllByParent(element.getName());
    }
}
