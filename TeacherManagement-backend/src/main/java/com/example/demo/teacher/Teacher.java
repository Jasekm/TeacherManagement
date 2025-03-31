package com.example.demo.teacher;



import com.example.demo.group.ClassTeacher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Comparator;
@Entity
@Table(name = "teacher")
public class Teacher  {
    @Id
    @SequenceGenerator(
            name="teacher_sequence",
            sequenceName = "teacher_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "teacher_sequence"
    )
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name="surname")
    private String surname;
    @Enumerated(EnumType.STRING)
    @Column(name="`condition`")
    private TeacherCondition condition;
    @Column(name="yob")
    private Integer yob;
    @Column(name="salary")
    private Double salary;
    @ManyToOne
    @JoinColumn(name = "class_teacher_id")
    @JsonIgnore
    private ClassTeacher classTeacher;

    public Teacher() {
    }

    public Teacher(Long id, String name, String surname, TeacherCondition condition, Integer yob, Double salary) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.condition = condition;
        this.yob = yob;
        this.salary = salary;
    }

    public Teacher(String name, String surname, TeacherCondition condition, Integer yob, Double salary) {
        this.name = name;
        this.surname = surname;
        this.condition = condition;
        this.yob = yob;
        this.salary = salary;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public TeacherCondition getCondition() {
        return condition;
    }

    public void setCondition(TeacherCondition condition) {
        this.condition = condition;
    }

    public Integer getYob() {
        return yob;
    }

    public void setYob(Integer yob) {
        this.yob = yob;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
    public ClassTeacher getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(ClassTeacher classTeacher) {
        this.classTeacher = classTeacher;
    }
    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", condition=" + condition +
                ", yob=" + yob +
                ", salary=" + salary +
                '}';
    }
}
