package com.example.telegrambotdemo;

import com.example.telegrambotdemo.model.Element;
import com.example.telegrambotdemo.repository.ElementRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ElementRepoTest {

    @Autowired
    private ElementRepo elementRepo;
    @BeforeEach
    public void init(){
        Element element1 = new Element(1L, "Element1",null,1);
        Element element2 = new Element(2L, "Element2", "Element1", 2);
        Element element3 = new Element(3L, "Element3", "Element2", 3);
        //adding elements to a database;
        elementRepo.save(element1);
        elementRepo.save(element2);
        elementRepo.save(element3);
    }

    @Test
    public void getAllTest(){
        Element element1 = new Element(1L, "Element1",null,1);
        Element element2 = new Element(2L, "Element2", "Element1", 2);
        Element element3 = new Element(3L, "Element3", "Element2", 3);

        List<Element> elements = elementRepo.findAll();

        Assertions.assertEquals(element1.getName(),elements.get(0).getName());
        Assertions.assertEquals(element2.getParent(),elements.get(0).getName());
        Assertions.assertEquals(element3,elements.get(2));
    }
    @Test
    public void deleteTest(){
        elementRepo.deleteById(3L);
        List<Element> elements = elementRepo.findAll();
        Assertions.assertTrue(elements.size()==2);
        Optional<Element> element = elementRepo.findByName("Element3");
        Assertions.assertTrue(element.isEmpty());
        Assertions.assertFalse(elements.size()==3);
    }
}
