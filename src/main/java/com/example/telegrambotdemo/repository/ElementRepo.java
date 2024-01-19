package com.example.telegrambotdemo.repository;

import com.example.telegrambotdemo.model.Element;
import jakarta.persistence.Transient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ElementRepo extends JpaRepository<Element, Long> {
    Optional<Element> findByName(String name);
    @Transactional
    void deleteAllByParent(String parent);
}
