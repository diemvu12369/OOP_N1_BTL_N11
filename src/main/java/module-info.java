module com.example.bomberman {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.media;
    requires java.desktop;
    requires json;

    opens bomberman to javafx.fxml;
    exports bomberman;
    exports bomberman.GlobalVariable;
    opens bomberman.GlobalVariable to javafx.fxml;
    exports bomberman.Object;
    opens bomberman.Object to javafx.fxml;
    exports bomberman.Object.NonMovingObject;
    opens bomberman.Object.NonMovingObject to javafx.fxml;
    exports bomberman.Object.MovingObject;
    opens bomberman.Object.MovingObject to javafx.fxml;
    exports bomberman.Object.MovingObject.Threats;
    opens bomberman.Object.MovingObject.Threats to javafx.fxml;
    exports bomberman.Map;
    opens bomberman.Map to javafx.fxml;
    exports bomberman.Object.MovingObject.Bomber;
    opens bomberman.Object.MovingObject.Bomber to javafx.fxml;
    exports bomberman.Server_Client;
    opens bomberman.Server_Client to javafx.fxml;
}