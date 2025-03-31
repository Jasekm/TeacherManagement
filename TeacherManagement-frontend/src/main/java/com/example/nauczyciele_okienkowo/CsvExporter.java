package com.example.nauczyciele_okienkowo;

import org.hibernate.Session;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {

    public void exportTeachersToCsv(String filePath) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            List<Teacher> teachers = session.createQuery("FROM Teacher", Teacher.class).list();

            writeTeachersCsv(teachers, filePath);

            System.out.println("Dane nauczycieli zostały wyeksportowane do pliku CSV.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void exportGroupsToCsv(String filePath) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            List<ClassTeacher> groups = session.createQuery("FROM ClassTeacher", ClassTeacher.class).list();

            writeGroupsCsv(groups, filePath);

            System.out.println("Dane grup nauczycieli zostały wyeksportowane do pliku CSV.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private void writeTeachersCsv(List<Teacher> teachers, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID;Imie;Nazwisko;Stan;Data Urodzenia;Wynagrodzenie\n");

            for (Teacher teacher : teachers) {
                writer.append(String.valueOf(teacher.getId())).append(";");
                writer.append(teacher.getImie()).append(";");
                writer.append(teacher.getNazwisko()).append(";");
                writer.append(teacher.getStan_nauczyciela().toString()).append(";");
                writer.append(String.valueOf(teacher.getRok_urodzenia())).append(";");
                writer.append(String.valueOf(teacher.getWynagrodzenie())).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisywania pliku CSV: " + e.getMessage());
        }
    }

    private void writeGroupsCsv(List<ClassTeacher> groups, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID;Nazwa grupy;Maksymalna liczba nauczycieli;Liczba nauczycieli\n");

            for (ClassTeacher group : groups) {
                writer.append(String.valueOf(group.getId())).append(";");
                writer.append(group.getNazwa_grupy()).append(";");
                writer.append(String.valueOf(group.getMaksymalna_ilosc_nauczycieli())).append(";");
                writer.append(String.valueOf(group.getLista_nauczycieli().size())).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisywania pliku CSV: " + e.getMessage());
        }
    }

}
