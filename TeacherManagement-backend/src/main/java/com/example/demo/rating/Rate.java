package com.example.demo.rating;
import com.example.demo.group.ClassTeacher;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @SequenceGenerator(
            name="rate_sequence",
            sequenceName = "rate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "rate_sequence"
    )
    private Long id;

    private int value;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private ClassTeacher classTeacher;

    private LocalDate date;

    private String comment;


    public Rate(int value, ClassTeacher classTeacher, LocalDate date, String comment) {
        this.value = value;
        this.classTeacher = classTeacher;
        this.date = date;
        this.comment = comment;
    }

    public Rate() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ClassTeacher getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(ClassTeacher classTeacher) {
        this.classTeacher = classTeacher;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
