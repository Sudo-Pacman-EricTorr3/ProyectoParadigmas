module mx.uaemex.fi.paradigmas.pptls {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;
    requires mx.uaemex.fi.paradigmas.pptls;

    opens mx.uaemex.fi.paradigmas.pptls to javafx.fxml;
    exports mx.uaemex.fi.paradigmas.pptls;
}