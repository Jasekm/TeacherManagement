package com.example.nauczyciele_okienkowo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class ManagerDanych {

    private static ManagerDanych instance;

    private Map<String, ClassTeacher> grupyNauczycielskie;
    private ObservableList<ClassTeacher> nauczycieleList;

    private String currentGroupName;

    private ManagerDanych() {
        grupyNauczycielskie = new HashMap<>();
        nauczycieleList = FXCollections.observableArrayList();
        currentGroupName = "";
        initializeDefaultData();

    }

    private void initializeDefaultData() {
        Teacher t1 = new Teacher("Jan", "Kowalski", TeacherCondition.OBECNY, 1975, 2100);
        Teacher t2 = new Teacher("Anna", "Nowak", TeacherCondition.CHORY, 1980, 1800);
        Teacher t3 = new Teacher("Piotr", "Zalewski", TeacherCondition.DELEGACJA, 1985, 2400);
        Teacher t4 = new Teacher("Maria", "Kowalczyk", TeacherCondition.NIEOBECNY, 1990, 3000);
        Teacher t5 = new Teacher("Tomasz", "Wójcik", TeacherCondition.OBECNY, 1978, 2100);
        Teacher t6 = new Teacher("Ewa", "Kwiatkowska", TeacherCondition.OBECNY, 1983, 2600);
        Teacher t7 = new Teacher("Krzysztof", "Pawlak", TeacherCondition.OBECNY, 1995, 3200);

        ClassTeacher fizycy = new ClassTeacher("Fizycy", 5);
        fizycy.addTeacher(t1);
        fizycy.addTeacher(t2);
        fizycy.addTeacher(t3);
        fizycy.addTeacher(t4);
        addGroup(fizycy);

        ClassTeacher chemicy = new ClassTeacher("Chemicy", 10);
        chemicy.addTeacher(t5);
        addGroup(chemicy);

        ClassTeacher matematycy = new ClassTeacher("Matematycy", 20);
        matematycy.addTeacher(t6);
        matematycy.addTeacher(t7);
        addGroup(matematycy);

    }

    public static ManagerDanych getInstance() {
        if (instance == null) {
            instance = new ManagerDanych();
        }
        return instance;
    }

    public void setTeacherList(ObservableList<ClassTeacher> nauczyciele) {
        this.nauczycieleList = nauczyciele;
    }

    public ObservableList<ClassTeacher> getTeacherList() {
        return nauczycieleList;
    }

    public void addGroup(ClassTeacher group) {
        nauczycieleList.add(group);
        grupyNauczycielskie.put(group.getNazwa_grupy(), group);
    }

    public void removeGroup(ClassTeacher group) {
        nauczycieleList.remove(group);
        grupyNauczycielskie.remove(group.getNazwa_grupy());
    }

    public ClassTeacher getGroup(String groupName) {
        return grupyNauczycielskie.get(groupName);
    }

    public Map<String, ClassTeacher> getAllGroups() {
        return grupyNauczycielskie;
    }

    public void addTeacherToGroup(String groupName, Teacher teacher) {
        try {
            ClassTeacher group = getGroup(groupName);
            if (group != null) {
                group.addTeacher(teacher);
            } else {
                throw new IllegalArgumentException("Grupa o podanej nazwie nie istnieje.");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public void removeTeacherFromGroup(String groupName, Teacher teacher) {
        ClassTeacher group = getGroup(groupName);
        if (group != null) {
            group.removeTeacher(teacher);
        }
    }

    public String getCurrentGroupName() {
        return currentGroupName;
    }

    public void setCurrentGroupName(String groupName) {
        this.currentGroupName = groupName;
    }

}
