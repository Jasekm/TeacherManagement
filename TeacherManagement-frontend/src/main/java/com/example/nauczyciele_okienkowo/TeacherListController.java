package com.example.nauczyciele_okienkowo;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class TeacherListController {

    @FXML
    private TableView<Teacher> teacherTable;

    @FXML
    private TableColumn<Teacher, String> teacherNameColumn;

    @FXML
    private TableColumn<Teacher, String> teacherSurnameColumn;

    @FXML
    private TableColumn<Teacher, String> teacherConditionColumn;

    @FXML
    private TableColumn<Teacher, Integer> teacherBirthYearColumn;

    @FXML
    private TableColumn<Teacher, Double> teacherSalaryColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private ComboBox<TeacherCondition> conditionComboBox;

    @FXML
    private TextField birthYearField;

    @FXML
    private TextField salaryField;
    @FXML
    private TextField searchField;
    private ObservableList<Teacher> teacherList;
    private Teacher selectedTeacher;
    private ManagerDanych managerDanych;
    private String currentGroupName;
    private Long currentGroupId;


    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) teacherTable.getScene().getWindow();
            currentGroupName = (String) stage.getUserData();
            System.out.println(currentGroupName);

            teacherList = FXCollections.observableArrayList();

            teacherNameColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getImie()));
            teacherSurnameColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getNazwisko()));
            teacherConditionColumn.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getStan_nauczyciela().toString()));
            teacherBirthYearColumn.setCellValueFactory(cellData ->
                    new SimpleIntegerProperty(cellData.getValue().getRok_urodzenia()).asObject());
            teacherSalaryColumn.setCellValueFactory(cellData ->
                    new SimpleDoubleProperty(cellData.getValue().getWynagrodzenie()).asObject());

            loadTeachersFromGroup();
            teacherTable.setItems(teacherList);
            conditionComboBox.setItems(FXCollections.observableArrayList(TeacherCondition.values()));
        });
    }


    private void loadTeachersFromGroup() {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();

            Query<ClassTeacher> groupQuery = session.createQuery(
                    "FROM ClassTeacher c WHERE c.nazwa_grupy = :groupName", ClassTeacher.class);
            groupQuery.setParameter("groupName", currentGroupName);

            ClassTeacher currentGroup = groupQuery.uniqueResult();

            if (currentGroup != null) {
                Long groupId = currentGroup.getId();  // Pobranie id grupy
                currentGroupId = groupId;
                Query<Teacher> teacherQuery = session.createQuery(
                        "FROM Teacher t WHERE t.classTeacher.id = :groupId", Teacher.class);
                teacherQuery.setParameter("groupId", groupId);

                List<Teacher> teachers = teacherQuery.getResultList();

                teacherList.setAll(teachers);
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onAddTeacherClicked() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        TeacherCondition condition = conditionComboBox.getValue();
        String birthYearText = birthYearField.getText();
        String salaryText = salaryField.getText();

        if (name.isEmpty() || surname.isEmpty() || condition == null || birthYearText.isEmpty() || salaryText.isEmpty()) {
            showAlert("Wszystkie pola muszą być wypełnione.");
            return;
        }

        try {
            int birthYear = Integer.parseInt(birthYearText);
            double salary = Double.parseDouble(salaryText);

            SessionFactory factory = HibernateUtil.getSessionFactory();
            try (Session session = factory.openSession()) {
                session.beginTransaction();

                ClassTeacher currentGroup = session.get(ClassTeacher.class, currentGroupId);
                if (currentGroup == null) {
                    showAlert("Grupa nauczycieli nie została znaleziona.");
                    return;
                }

                Teacher existingTeacher = session.createQuery("FROM Teacher WHERE lower(imie) = :name AND lower(nazwisko) = :surname", Teacher.class)
                        .setParameter("name", name.toLowerCase())
                        .setParameter("surname", surname.toLowerCase())
                        .uniqueResult();

                if (existingTeacher != null) {
                    showAlert("Nauczyciel o tym imieniu i nazwisku już istnieje.");
                    return;  // Zakończenie, jeśli nauczyciel już istnieje
                }

                Teacher newTeacher = new Teacher(name, surname, condition, birthYear, salary);

                newTeacher.setClassTeacher(currentGroup);

                session.save(newTeacher);
                session.getTransaction().commit();

                loadTeachersFromGroup();
                teacherTable.refresh();

                clearFields();

            } catch (Exception e) {
                showAlert("Wystąpił błąd przy dodawaniu nauczyciela.");
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            showAlert("Rok urodzenia musi być liczbą całkowitą, a wynagrodzenie liczbą.");
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
        } catch (Exception e) {
            showAlert("Wystąpił nieoczekiwany błąd.");
            e.printStackTrace();
        }
    }


    private void clearFields() {
        nameField.clear();
        surnameField.clear();
        conditionComboBox.setValue(null);
        birthYearField.clear();
        salaryField.clear();
    }

    @FXML
    private void onCloseButtonClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/nauczyciele_okienkowo/TeacherGroupApp.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) teacherTable.getScene().getWindow();
            stage.setScene(scene);  // Set the new scene

            stage.setTitle("Grupy Nauczycieli");

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Wystąpił błąd przy ładowaniu nowej sceny.");
        }
    }

    @FXML
    public void onRemoveTeacherClicked(ActionEvent actionEvent) {
        if (selectedTeacher == null) {
            showAlert("Proszę wybrać nauczyciela do usunięcia.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.delete(selectedTeacher);

            session.getTransaction().commit();

            if (!searchField.getText().isEmpty()) {
                onSearchFieldChanged();
            } else {
                loadTeachersFromGroup();
                teacherTable.refresh();
            }

            teacherTable.getSelectionModel().clearSelection();
            selectedTeacher = null;

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Wystąpił problem podczas usuwania nauczyciela.");
        }

        teacherTable.getSelectionModel().clearSelection();
        selectedTeacher = null;
    }


    public void onChangeScene(MouseEvent event) {
        clearFields();
        MouseButton button = event.getButton();
        if (button == MouseButton.PRIMARY) {
            selectedTeacher = teacherTable.getSelectionModel().getSelectedItem();

        } else if (button == MouseButton.SECONDARY) {
            selectedTeacher = teacherTable.getSelectionModel().getSelectedItem();
            if (selectedTeacher != null) {
                nameField.setText(selectedTeacher.getImie());
                surnameField.setText(selectedTeacher.getNazwisko());
                conditionComboBox.setValue(selectedTeacher.getStan_nauczyciela());
                birthYearField.setText(String.valueOf(selectedTeacher.getRok_urodzenia()));
                salaryField.setText(String.valueOf(selectedTeacher.getWynagrodzenie()));
            }
        }
    }

    @FXML
    public void onModifyButtonClicked(ActionEvent actionEvent) {
        if (selectedTeacher == null) {
            showAlert("Proszę wybrać nauczyciela do edycji.");
            return;
        }

        String name = nameField.getText();
        String surname = surnameField.getText();
        TeacherCondition condition = conditionComboBox.getValue();
        String birthYearText = birthYearField.getText();
        String salaryText = salaryField.getText();

        if (name.isEmpty() || surname.isEmpty() || condition == null || birthYearText.isEmpty() || salaryText.isEmpty()) {
            showAlert("Wszystkie pola muszą być wypełnione.");
            return;
        }

        try {
            int birthYear = Integer.parseInt(birthYearText);
            double salary = Double.parseDouble(salaryText);

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                session.beginTransaction();

                Teacher existingTeacher = session.createQuery(
                                "FROM Teacher WHERE lower(imie) = :name AND lower(nazwisko) = :surname " +
                                        "AND stan_nauczyciela = :condition AND rok_urodzenia = :birthYear AND wynagrodzenie = :salary", Teacher.class)
                        .setParameter("name", name.toLowerCase())
                        .setParameter("surname", surname.toLowerCase())
                        .setParameter("condition", condition)
                        .setParameter("birthYear", birthYear)
                        .setParameter("salary", salary)
                        .uniqueResult();

                if (existingTeacher != null && !existingTeacher.equals(selectedTeacher)) {
                    showAlert("Nauczyciel o podanych danych już istnieje.");
                    return;
                }

                selectedTeacher.setImie(name);
                selectedTeacher.setNazwisko(surname);
                selectedTeacher.setStan_nauczyciela(condition);
                selectedTeacher.setRok_urodzenia(birthYear);
                selectedTeacher.setWynagrodzenie(salary);

                session.update(selectedTeacher);

                session.getTransaction().commit();

                loadTeachersFromGroup();
                teacherTable.refresh();

                clearFields();
                selectedTeacher = null;

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukces");
                alert.setHeaderText(null);
                alert.setContentText("Dane nauczyciela zostały zmienione.");
                alert.showAndWait();
            }

        } catch (NumberFormatException e) {
            showAlert("Rok urodzenia musi być liczbą całkowitą, a wynagrodzenie liczbą.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Wystąpił problem podczas edycji nauczyciela.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onSearchFieldChanged() {
        String searchText = searchField.getText().toLowerCase();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            List<Teacher> teachers = session.createQuery("FROM Teacher WHERE classTeacher.id = :groupId", Teacher.class)
                    .setParameter("groupId", currentGroupId)
                    .list();

            ObservableList<Teacher> filteredList = FXCollections.observableArrayList();

            for (Teacher teacher : teachers) {
                if (teacher.getImie().toLowerCase().contains(searchText) ||
                        teacher.getNazwisko().toLowerCase().contains(searchText)) {
                    filteredList.add(teacher);
                }
            }

            teacherTable.setItems(filteredList);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Błąd podczas wyszukiwania nauczycieli.");
        }
    }

    @FXML
    private void onExportCsvClicked() {
        String filePath = "C:\\Users\\Mateusz\\Desktop\\data\\teachers.csv";

        CsvExporter exporter = new CsvExporter();

        exporter.exportTeachersToCsv(filePath);

    }


}
