package com.example.demo.group;

import com.example.demo.teacher.Teacher;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teacher_group")
public class ClassTeacher {
    @Id
    @SequenceGenerator(
            name="group_sequence",
            sequenceName = "group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_sequence"
    )

    private Long id;
    private String name;
    private Integer max_teachers;
    @OneToMany(mappedBy = "classTeacher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Teacher> lista_nauczycieli = new ArrayList<>();

    public ClassTeacher() {

    }

    public ClassTeacher(Long id, String name, Integer max_teachers) {
        this.id = id;
        this.name = name;
        this.max_teachers = max_teachers;
    }

    public ClassTeacher(String name, Integer max_teachers) {
        this.name = name;
        this.max_teachers = max_teachers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMax_teachers() {
        return max_teachers;
    }

    public void setMax_teachers(Integer max_teachers) {
        this.max_teachers = max_teachers;
    }
    public List<Teacher> getTeachers() {
        return lista_nauczycieli;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.lista_nauczycieli = teachers;
    }
    @Override
    public String toString() {
        return "ClassTeacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", max_teachers=" + max_teachers +
                '}';
    }
}
