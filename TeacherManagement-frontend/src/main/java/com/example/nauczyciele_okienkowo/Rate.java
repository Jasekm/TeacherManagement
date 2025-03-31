package com.example.nauczyciele_okienkowo;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
