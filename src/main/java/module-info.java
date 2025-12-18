module mx.uaemex.fi.paradigmas.pptls {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens mx.uaemex.fi.paradigmas.pptls to javafx.fxml;


    opens mx.uaemex.fi.paradigmas.pptls.controller to javafx.fxml;

    opens mx.uaemex.fi.paradigmas.pptls.model.data to javafx.base;

    exports mx.uaemex.fi.paradigmas.pptls;
    exports mx.uaemex.fi.paradigmas.pptls.controller;
}