module Lian3Project {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires Medusa;
    opens model to javafx.fxml, javafx.controls, javafx.graphics;
    opens view to  javafx.fxml, javafx.controls, javafx.graphics;
    opens viewModel to  javafx.fxml, javafx.controls, javafx.graphics;
    opens view.viewlist to javafx.fxml;
    opens view.openfiles  to javafx.fxml;
    opens view.graphs  to javafx.fxml;
    opens view.buttons  to javafx.fxml;
    opens view.joystick to javafx.fxml;
    opens Algos to javafx.controls, javafx.fxml, javafx.graphics;

}