package com.example.nauczyciele_okienkowo;

import jakarta.persistence.*;

import java.util.Comparator;

@Entity
@Table(name = "teacher")
public class Teacher implements Comparable<Teacher> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "imie")
    String imie;
    @Column(name = "nazwisko")
    String nazwisko;
    @Enumerated(EnumType.STRING)
    @Column(name = "stan_nauczyciela")
    TeacherCondition stan_nauczyciela;
    @Column(name = "rok_urodzenia")
    int rok_urodzenia;
    @Column(name = "wynagrodzenie")
    double wynagrodzenie;
    @ManyToOne
    @JoinColumn(name = "class_teacher_id")
    private ClassTeacher classTeacher;

    public Teacher(String imie, String nazwisko, TeacherCondition stan_nauczyciela, int rok_urodzenia, double wynagrodzenie) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.stan_nauczyciela = stan_nauczyciela;
        this.rok_urodzenia = rok_urodzenia;
        this.wynagrodzenie = wynagrodzenie;
    }

    public Teacher() {

    }

    public void print() {
        System.out.println("Imie: " + this.imie);
        System.out.println("Nazwisko: " + this.nazwisko);
        System.out.println("Stan nauczyciela: " + this.stan_nauczyciela);
        System.out.println("Rok urodzenia: " + this.rok_urodzenia);
        System.out.println("Wynagrodzenie: " + this.wynagrodzenie);
        System.out.println("");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getImie() {
        return imie;
    }

    public double getWynagrodzenie() {
        return wynagrodzenie;
    }

    public int getRok_urodzenia() {
        return rok_urodzenia;
    }

    public TeacherCondition getStan_nauczyciela() {
        return stan_nauczyciela;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public void setStan_nauczyciela(TeacherCondition stan_nauczyciela) {
        this.stan_nauczyciela = stan_nauczyciela;
    }

    public void setRok_urodzenia(int rok_urodzenia) {
        this.rok_urodzenia = rok_urodzenia;
    }

    public void setWynagrodzenie(double wynagrodzenie) {
        this.wynagrodzenie = wynagrodzenie;
    }

    public void setClassTeacher(ClassTeacher classTeacher) {
        this.classTeacher = classTeacher;
    }

    public ClassTeacher getClassTeacher() {
        return classTeacher;
    }

    @Override
    public int compareTo(Teacher o) {

        int porownaneNazwiska = nazwisko.compareTo(o.nazwisko);

        if (porownaneNazwiska == 0) {
            return imie.compareTo(o.imie);
        } else {
            return porownaneNazwiska;
        }
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", stan_nauczyciela=" + stan_nauczyciela +
                ", rok_urodzenia=" + rok_urodzenia +
                ", wynagrodzenie=" + wynagrodzenie +
                '}';
    }
}
