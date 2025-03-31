package com.example.nauczyciele_okienkowo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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

import java.io.IOException;
import java.util.List;

public class TeacherGroupController {

    @FXML
    private TableView<ClassTeacher> tableView;

    @FXML
    private TableColumn<ClassTeacher, String> nazwaGrupyColumn;

    @FXML
    private TableColumn<ClassTeacher, Integer> liczbaNauczycieliColumn;

    @FXML
    private TableColumn<ClassTeacher, Integer> maxNauczycieliColumn;

    @FXML
    private TableColumn<ClassTeacher, String> procentoweZapelnienieColumn;
    @FXML
    private TableColumn<ClassTeacher, Integer> liczbaOcenPowyzejSredniejColumn;

    @FXML
    private TextField nazwaGrupyField;

    @FXML
    private TextField maxNauczycieliField;

    @FXML
    private TextField searchField;

    private ObservableList<ClassTeacher> groupList;

    private ClassTeacher selectedGroup;

    private void loadGroups() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            List<ClassTeacher> groups = session.createQuery("FROM ClassTeacher", ClassTeacher.class).list();

            groupList.clear();

            groupList.addAll(groups);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Błąd podczas ładowania grup.");
        }
    }


    @FXML
    public void initialize() {
        groupList = FXCollections.observableArrayList();

        nazwaGrupyColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNazwa_grupy()));
        maxNauczycieliColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getMaksymalna_ilosc_nauczycieli()).asObject());
        liczbaNauczycieliColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getLista_nauczycieli().size()).asObject());

        procentoweZapelnienieColumn.setCellValueFactory(cellData -> {
            int liczbaNauczycieli = cellData.getValue().getLista_nauczycieli().size();
            int maxNauczycieli = cellData.getValue().getMaksymalna_ilosc_nauczycieli();
            double procentZapelnienia = maxNauczycieli > 0 ?
                    (liczbaNauczycieli / (double) maxNauczycieli) * 100 : 0;
            return new SimpleStringProperty(String.format("%.2f%%", procentZapelnienia));
        });
        liczbaOcenPowyzejSredniejColumn.setCellValueFactory(cellData -> {
            ClassTeacher group = cellData.getValue();
            int liczbaOcenPowyzejSredniej = countRatingsAboveThreshold(group);
            return new SimpleIntegerProperty(liczbaOcenPowyzejSredniej).asObject();
        });
        loadGroups();
        tableView.setItems(groupList);
    }

    private int countRatingsAboveThreshold(ClassTeacher group) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);

            Root<Rate> rateRoot = cq.from(Rate.class);

            cq.select(cb.count(rateRoot)) // Count the ratings
                    .where(
                            cb.equal(rateRoot.get("classTeacher"), group),
                            cb.greaterThan(rateRoot.get("value"), 3)
                    );

            Long count = session.createQuery(cq).getSingleResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Błąd podczas obliczania liczby ocen powyżej 3.");
            return 0;
        }
    }


    @FXML
    private void onAddGroupClicked() {
        String nazwa = nazwaGrupyField.getText();
        String pojemnosc = maxNauczycieliField.getText();

        if (nazwa.isEmpty() || pojemnosc.isEmpty()) {
            showAlert("Wszystkie pola muszą być wypełnione.");
            return;
        }

        try {
            int maxNauczycieli = Integer.parseInt(pojemnosc);
            String nazwaLower = nazwa.toLowerCase();

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                session.beginTransaction();

                ClassTeacher existingGroup = session.createQuery("FROM ClassTeacher WHERE lower(nazwa_grupy) = :nazwa", ClassTeacher.class)
                        .setParameter("nazwa", nazwaLower)
                        .uniqueResult();

                if (existingGroup != null) {
                    showAlert("Grupa o tej nazwie już istnieje.");
                } else {
                    ClassTeacher newGroup = new ClassTeacher(nazwa, maxNauczycieli);
                    session.save(newGroup);

                    session.getTransaction().commit();

                    loadGroups();


                }
            }
        } catch (NumberFormatException ex) {
            showAlert("Maksymalna liczba nauczycieli musi być liczbą całkowitą.");
        }

        tableView.getSelectionModel().clearSelection();
        loadGroups();
    }

    @FXML
    private void onRemoveGroupClicked() {
        if (selectedGroup == null) {
            showAlert("Proszę wybrać grupę do usunięcia.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.delete(selectedGroup);

            session.getTransaction().commit();

            if (!searchField.getText().isEmpty()) {
                onSearchFieldChanged();
            } else {
                loadGroups();
                tableView.refresh();
            }
            tableView.getSelectionModel().clearSelection();
            selectedGroup = null;


        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Wystąpił problem podczas usuwania grupy.");
        }

        tableView.getSelectionModel().clearSelection();
        selectedGroup = null;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void onChangeScene(MouseEvent event) {
        MouseButton button = event.getButton();

        if (button == MouseButton.SECONDARY) {
            selectedGroup = tableView.getSelectionModel().getSelectedItem();
            if (selectedGroup != null) {
                nazwaGrupyField.setText(selectedGroup.getNazwa_grupy());
                maxNauczycieliField.setText(String.valueOf(selectedGroup.getMaksymalna_ilosc_nauczycieli()));
            }
        } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
            clearFields();
            selectedGroup = tableView.getSelectionModel().getSelectedItem();
        } else if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            try {
                selectedGroup = tableView.getSelectionModel().getSelectedItem();
                String groupName = selectedGroup.getNazwa_grupy();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/nauczyciele_okienkowo/TeacherListApp.fxml"));
                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) tableView.getScene().getWindow();
                stage.setUserData(groupName);
                stage.setScene(scene);
                stage.setTitle("Lista Nauczycieli");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Wystąpił błąd przy ładowaniu nowej sceny.");
            }
        }
    }

    @FXML
    public void onModifyButtonClicked(ActionEvent actionEvent) {
        if (selectedGroup == null) {
            showAlert("Proszę wybrać grupę do edycji.");
            return;
        }

        String newName = nazwaGrupyField.getText();
        String newMaxTeachersText = maxNauczycieliField.getText();

        if (newName.isEmpty() || newMaxTeachersText.isEmpty()) {
            showAlert("Wszystkie pola muszą być wypełnione.");
            return;
        }

        try {
            int newMaxTeachers = Integer.parseInt(newMaxTeachersText);

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                session.beginTransaction();

                ClassTeacher existingGroup = session.createQuery("FROM ClassTeacher WHERE lower(nazwa_grupy) = :nazwa AND maksymalna_ilosc_nauczycieli = :maxNauczycieli", ClassTeacher.class)
                        .setParameter("nazwa", newName.toLowerCase())
                        .setParameter("maxNauczycieli", newMaxTeachers)
                        .uniqueResult();

                if (existingGroup != null && !existingGroup.getNazwa_grupy().equalsIgnoreCase(selectedGroup.getNazwa_grupy())) {
                    showAlert("Grupa o tej nazwie już istnieje.");
                    return;
                }

                selectedGroup.setNazwa_grupy(newName);
                selectedGroup.setMaksymalna_ilosc_nauczycieli(newMaxTeachers);

                session.update(selectedGroup);

                session.getTransaction().commit();

                loadGroups();

                clearFields();
                tableView.getSelectionModel().clearSelection();
                selectedGroup = null;

            }

        } catch (NumberFormatException ex) {
            showAlert("Maksymalna liczba nauczycieli musi być liczbą całkowitą.");
        }
    }

    @FXML
    private void onSearchFieldChanged() {
        String searchText = searchField.getText().toLowerCase();
        loadGroups();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            List<ClassTeacher> groups = session.createQuery("FROM ClassTeacher", ClassTeacher.class).list();

            ObservableList<ClassTeacher> filteredList = FXCollections.observableArrayList();

            for (ClassTeacher group : groups) {
                if (group.getNazwa_grupy().toLowerCase().contains(searchText)) {
                    filteredList.add(group);
                }
            }

            tableView.setItems(filteredList);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Błąd podczas wyszukiwania.");
        }
    }

    private void clearFields() {
        nazwaGrupyField.clear();
        maxNauczycieliField.clear();
    }

    @FXML
    private void onExportCsvClicked() {
        String filePath = "C:\\Users\\Mateusz\\Desktop\\data\\teachers_group.csv";

        CsvExporter exporter = new CsvExporter();

        exporter.exportGroupsToCsv(filePath);

    }

    @FXML
    private void onAddRateClicked() {
        ClassTeacher selectedGroup = tableView.getSelectionModel().getSelectedItem();

        if (selectedGroup != null) {
            RateDialogController rateDialogController = new RateDialogController();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/nauczyciele_okienkowo/RateDialog.fxml"));

            try {
                Stage stage = new Stage();
                stage.setUserData(selectedGroup.getId());
                stage.setScene(new Scene(loader.load()));
                stage.setOnHiding(event -> loadGroups());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showAlert("Proszę wybrać grupę, do której chcesz dodać ocenę.");

        }
    }

}
