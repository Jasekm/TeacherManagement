package com.example.nauczyciele_okienkowo;

import java.util.Comparator;

public class SalaryComparator implements Comparator<Teacher> {

    @Override
    public int compare(Teacher o1, Teacher o2) {
        int result = Double.compare(o1.getWynagrodzenie(),o2.getWynagrodzenie());

        return -1* result;
    }
}
