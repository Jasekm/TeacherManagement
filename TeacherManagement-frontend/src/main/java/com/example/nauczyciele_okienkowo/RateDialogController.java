package com.example.nauczyciele_okienkowo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;

public class RateDialogController {

    @FXML
    private TextField rateValueField;
    @FXML
    private TextArea commentField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Long groupId;

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            Stage stage = (Stage) rateValueField.getScene().getWindow();
            groupId = (Long) stage.getUserData();

            if (datePicker != null) {
                datePicker.setValue(LocalDate.now());
            }
        });
    }

    @FXML
    private void onSaveButtonClicked() {
        System.out.println("ID grupy przed zapisem: " + groupId);
        String rateText = rateValueField.getText();
        int rateValue;
        try {
            rateValue = Integer.parseInt(rateText);
            if (rateValue < 0 || rateValue > 6) {
                System.out.println("Wartość oceny musi być między 0 a 6.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Proszę podać poprawną wartość liczbową dla oceny.");
            return;
        }

        String comment = commentField.getText();

        LocalDate date = datePicker.getValue();
        if (date == null) {
            System.out.println("Proszę wybrać datę.");
            return;
        }

        if (groupId == null) {
            System.out.println("ID grupy jest puste!");
            return;
        }

        System.out.println("Group ID: " + groupId);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ClassTeacher classTeacher = session.get(ClassTeacher.class, groupId);

            if (classTeacher != null) {
                Rate rate = new Rate(rateValue, classTeacher, date, comment);
                RateDAO rateDAO = new RateDAO();
                rateDAO.saveRate(rate);
            } else {
                System.out.println("Grupa o podanym ID nie została znaleziona.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Wystąpił błąd podczas zapisywania oceny.");
        }

        closeDialog();
    }


    @FXML
    private void onCancelButtonClicked() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
