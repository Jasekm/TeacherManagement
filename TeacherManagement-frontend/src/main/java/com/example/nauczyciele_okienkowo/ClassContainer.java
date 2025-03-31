package com.example.nauczyciele_okienkowo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassContainer {
    Map<String,ClassTeacher> grupy_nauczycielskie;

    public ClassContainer() {
        this.grupy_nauczycielskie = new HashMap<>();
    }
    public void addClass(String nazwa, int pojemnosc){
        if (!grupy_nauczycielskie.containsKey(nazwa)) {
            grupy_nauczycielskie.put(nazwa, new ClassTeacher(nazwa, pojemnosc));
            System.out.println("Dodano nową grupę: " + nazwa + " z pojemnością: " + pojemnosc);
        } else {
            System.out.println("Grupa o nazwie " + nazwa + " już istnieje.");
        }

    }
    public void removeClass(String nazwa){
        this.grupy_nauczycielskie.remove(nazwa);
    }
    public List<String> findEmpty() {
        List<String> pusteGrupy = new ArrayList<>();

        for (Map.Entry<String, ClassTeacher> entry : grupy_nauczycielskie.entrySet()) {
            ClassTeacher grupa = entry.getValue();
            if (grupa.getLista_nauczycieli().isEmpty()) {
                pusteGrupy.add(entry.getKey());
            }
        }

        return pusteGrupy;
    }
    public void summary(){
        for (Map.Entry<String, ClassTeacher> entry : grupy_nauczycielskie.entrySet()) {
            String nazwaGrupy = entry.getKey();
            ClassTeacher grupa = entry.getValue();
            double zapelnienie = (double) grupa.getLista_nauczycieli().size() / grupa.getMaksymalna_ilosc_nauczycieli() *100;

            System.out.println("Nazwa grupy: "+nazwaGrupy+" zapelnienie procentowe: "+zapelnienie+"%");
        }
        System.out.println(" ");

    }
    public void print(){
        for (Map.Entry<String, ClassTeacher> entry : grupy_nauczycielskie.entrySet()) {
            String nazwaGrupy = entry.getKey();
            System.out.print(nazwaGrupy+ " ");
        }
        System.out.println(" ");

    }

}
