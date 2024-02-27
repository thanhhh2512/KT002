module com.k2t.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.k2t.demo to javafx.fxml;
    exports com.k2t.demo;
    exports com.k2t.demo.controllers;
    opens com.k2t.demo.controllers to javafx.fxml;

}