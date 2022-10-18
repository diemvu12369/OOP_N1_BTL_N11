package bomberman.GlobalVariable;

import bomberman.BombermanApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;

public class RenderVariable {
    //************************ CÁC THÔNG SỐ RENDER ************************************************

    public static double SCREEN_WIDTH = 520;
    public static double SCREEN_LENGTH = 1240;

    // *********************** BIẾN ĐƯỢC SỬ DỤNG GLOBAL ĐỂ RENDER *********************************
    public static Group root = new Group();

    public static Canvas canvas = new Canvas(SCREEN_LENGTH, SCREEN_WIDTH);

    public static GraphicsContext gc = canvas.getGraphicsContext2D();

    public static Scene scene = new Scene(root);

    static FXMLLoader loaderScene1 = new FXMLLoader(BombermanApplication.class.getResource("/start.fxml"));

    public static Scene menuScene;

    public static boolean stateSound = true;

    static {
        try {
            menuScene = new Scene(loaderScene1.load(), SCREEN_LENGTH, SCREEN_WIDTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RenderVariable() throws IOException {
    }

    public static void setStateSound() {
        stateSound = !stateSound;
    }
}
