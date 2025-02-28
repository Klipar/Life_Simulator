module com.life_simulator {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.life_simulator to javafx.fxml;
    exports com.life_simulator;
}
