module com.timemanagement.timemanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;
    requires jdk.xml.dom;
    requires atlantafx.base;
    requires org.jetbrains.annotations;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;
    requires com.calendarfx.view;
    requires org.kordamp.ikonli.material2;

    opens com.timemanagement to javafx.fxml;
    exports com.timemanagement;
    exports com.timemanagement.Controllers;
    exports com.timemanagement.Models;
    exports com.timemanagement.Views;
}