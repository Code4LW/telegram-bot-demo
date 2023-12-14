package com.example.telegrambotdemo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Data
@Table(name = "element")
@AllArgsConstructor
@NoArgsConstructor
public class Element {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "parent_element")
    private String parent;
    @Column(name = "branch_level")
    private Integer level=1;

    public boolean hasParent(){
        if(this.getParent()==null) return false;
        return true;
    }
}
