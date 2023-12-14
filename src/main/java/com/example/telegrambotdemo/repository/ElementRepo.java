package com.example.telegrambotdemo.repository;

import com.example.telegrambotdemo.model.Element;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElementRepo extends JpaRepository<Element, Long> {
    Optional<Element> findByName(String name);
    void deleteAllByParent(String parent);
}
