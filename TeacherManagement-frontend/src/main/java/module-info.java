module com.example.nauczyciele_okienkowo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    opens com.example.nauczyciele_okienkowo to javafx.fxml, org.hibernate.orm.core;
    exports com.example.nauczyciele_okienkowo;
}