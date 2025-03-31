package com.example.nauczyciele_okienkowo;

import java.util.Comparator;

public class NameComparator implements Comparator<Teacher> {

    @Override
    public int compare(Teacher o1, Teacher o2) {
        int result = o1.getImie().compareTo(o2.getImie());
        return result;
    }
}
