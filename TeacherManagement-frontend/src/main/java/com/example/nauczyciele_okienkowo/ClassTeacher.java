
package com.example.nauczyciele_okienkowo;

import com.example.nauczyciele_okienkowo.Teacher;
import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "teacher_group")
public class ClassTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nazwa_grupy")
    private String nazwa_grupy;

    @Column(name = "maksymalna_ilosc_nauczycieli")
    private int maksymalna_ilosc_nauczycieli;

    @OneToMany(mappedBy = "classTeacher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Teacher> lista_nauczycieli = new ArrayList<>();
    @OneToMany(mappedBy = "classTeacher")
    private Set<Rate> rates;

    public ClassTeacher() {
    }

    public ClassTeacher(String nazwa_grupy, int maksymalna_ilosc_nauczycieli) {
        this.nazwa_grupy = nazwa_grupy;
        this.lista_nauczycieli = new ArrayList<>();
        this.maksymalna_ilosc_nauczycieli = maksymalna_ilosc_nauczycieli;
    }

    //a
    public void addTeacher(Teacher nauczyciel) {
        boolean check = false;
        for (int i = 0; i < this.lista_nauczycieli.size(); i++) {
            int result = nauczyciel.compareTo(lista_nauczycieli.get(i));
            if (result == 0) {
                check = true;
            }
        }
        if (check == true) {
            //System.out.println("Nauczyciel jest juz na liscie");
            throw new IllegalArgumentException("Nauczyciel już istnieje w tej grupie.");
        } else {
            if (maksymalna_ilosc_nauczycieli > lista_nauczycieli.size()) {
                lista_nauczycieli.add(nauczyciel);
            } else {
                // System.out.println("Maksymalna liczba nauczycieli w grupie");
                throw new IllegalArgumentException("Maksymalna liczba nauczycieli w grupie została osiągnięta.");
            }
        }
    }

    //b
    public void addSalary(Teacher nauczyciel, double suma) {

        nauczyciel.wynagrodzenie += suma;
    }

    //c
    public void removeTeacher(Teacher nauczyciel) {
        this.lista_nauczycieli.remove(this.lista_nauczycieli.indexOf(nauczyciel));
    }

    //d
    public void changeCondition(Teacher nauczyciel, TeacherCondition stan) {
        nauczyciel.stan_nauczyciela = stan;
    }
    //e

    //f
    public void searchPartial(String fragment) {
        String fragmentLower = fragment.toLowerCase();
        boolean check = false;
        for (Teacher nauczyciel : this.lista_nauczycieli) {
            if (nauczyciel.getImie().toLowerCase().contains(fragmentLower) ||
                    nauczyciel.getNazwisko().toLowerCase().contains(fragmentLower)) {
                nauczyciel.print();
                check = true;
            }
        }
        if (check == false) {
            System.out.println("Nie znaleziono osoby ktorej dane zawieraja podany fragment.");
        }
    }

    //g
    public void countByCondition(TeacherCondition stan) {
        int result = 0;
        for (int i = 0; i < this.lista_nauczycieli.size(); i++) {
            if (this.lista_nauczycieli.get(i).stan_nauczyciela == stan) {
                result++;
            }
        }
        System.out.print("Ilosc nauczycieli o stanie " + stan + " : " + result);
    }

    //h
    public void summary() {

        this.lista_nauczycieli.forEach((e) -> e.print());
    }

    //i
    public List<Teacher> sortByName() {
        List<Teacher> lista_posortowana = this.lista_nauczycieli;

        Collections.sort(lista_posortowana, new NameComparator());
        return lista_posortowana;
    }

    //j
    public List<Teacher> sortBySalary() {
        List<Teacher> lista_posortowana = this.lista_nauczycieli;

        Collections.sort(lista_posortowana, new SalaryComparator());
        return lista_posortowana;
    }

    public void max() {
        System.out.println(Collections.max(this.lista_nauczycieli, Collections.reverseOrder(new SalaryComparator())));

    }

    public void print() {
        System.out.println("Informacje o grupie nauczycieli:");
        System.out.println("Nazwa grupy: " + nazwa_grupy);
        System.out.println("Maksymalna liczba nauczycieli: " + maksymalna_ilosc_nauczycieli);
        System.out.println("Obecna liczba nauczycieli: " + lista_nauczycieli.size());

        System.out.println("\nLista nauczycieli w grupie:");
        if (lista_nauczycieli.isEmpty()) {
            System.out.println("Brak nauczycieli w grupie.");
        } else {
            for (Teacher teacher : lista_nauczycieli) {
                teacher.print();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Teacher> getLista_nauczycieli() {
        return lista_nauczycieli;
    }

    public String getNazwa_grupy() {
        return nazwa_grupy;
    }

    public int getMaksymalna_ilosc_nauczycieli() {
        return maksymalna_ilosc_nauczycieli;
    }

    public void setNazwa_grupy(String nazwa_grupy) {
        this.nazwa_grupy = nazwa_grupy;
    }

    public void setLista_nauczycieli(ArrayList<Teacher> lista_nauczycieli) {
        this.lista_nauczycieli = lista_nauczycieli;
    }

    public void setMaksymalna_ilosc_nauczycieli(int maksymalna_ilosc_nauczycieli) {
        this.maksymalna_ilosc_nauczycieli = maksymalna_ilosc_nauczycieli;
    }
}
