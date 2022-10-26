package bomberman;

import bomberman.GlobalVariable.*;
import bomberman.Server_Client.Client;
import bomberman.Server_Client.EchoThread;
import bomberman.Server_Client.Server;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.json.JSONArray;

import javax.sound.sampled.FloatControl;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

/**
 * Chương trình chính.
 */
public class BombermanApplication extends Application {
    private enum Mode {
        MENU,  //TODO: code this
        PvP,
        PvB,
        PvP_IN_EXECUTION,
    }

    static Mode currentMode = Mode.MENU;
    static boolean hasChanged = true;

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        Group root = RenderVariable.root;
        Canvas canvas = RenderVariable.canvas;
        Scene scene = RenderVariable.scene;

        root.getChildren().add(canvas);
        scene.setRoot(root);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if (hasChanged) {
                    if (currentMode == Mode.MENU) {
                        try {
                            setupMenuMode(stage);
                        } catch (IOException event) {
                            event.printStackTrace();
                        }
                    } else if (currentMode == Mode.PvB) {
                        setupPvBMode(stage);
                    } else if (currentMode == Mode.PvP) {
                        setupPvPMode(stage);
                    } else if (currentMode == Mode.PvP_IN_EXECUTION) {
                        executePvPGame(stage);
                    }
                }

                if (!stage.isShowing()) {

                    try {
                        if (LANVariables.server != null && !LANVariables.server.serverSocket.isClosed()) {
                            LANVariables.server.serverSocket.close();
                        }
                        if (LANVariables.client != null && !LANVariables.client.socket.isClosed()) {
                            LANVariables.client.socket.close();
                        }
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                    LANVariables.client = null;
                    LANVariables.server = null;
                    GameVariables.playerRole = null;
                    GameVariables.commandList = new JSONArray();
                    GameVariables.temporaryCommandList = new JSONArray();
                    GameVariables.commandListString = new String();
                    GameVariables.PvP = null;
                }
            }
        }.start();

        stage.show();
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primaryScreenBounds.getWidth() - RenderVariable.SCREEN_LENGTH) / 2);
        stage.setY((primaryScreenBounds.getHeight() - RenderVariable.SCREEN_WIDTH) / 2);
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setupPvBMode(Stage stage) {
        hasChanged = false;
        currentMode = Mode.PvB;

        GameVariables.PvB = new PvB_GamePlay();

        GameVariables.PvB.render();
        GameVariables.PvB.playBackgroundAudio();

        RenderVariable.scene.setOnMouseClicked(mouseEvent -> {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();

            if (x >= 1201 && y <= 39) {
                RenderVariable.setStateSound();
                FloatControl volume = (FloatControl) FilesPath.PlayGroundAudio.getControl(FloatControl.Type.MASTER_GAIN);
                if (!RenderVariable.stateSound) {
                    volume.setValue(volume.getMinimum());
                }
                else {
                    volume.setValue(6);
                }
            }
        });

        RenderVariable.scene.setOnKeyPressed(GameVariables.PvB::inputPressedKey);
        RenderVariable.scene.setOnKeyReleased(GameVariables.PvB::inputReleasedKey);

        GameVariables.PvB.setStatus(PvB_GamePlay.typeOfStatus.ONGOING);
        new AnimationTimer() {
            boolean stopped = false;

            public void handle(long currentNanoTime) {

                if (stopped) {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException event) {
                        event.printStackTrace();
                    }
                    currentMode = Mode.MENU;
                }

                if (GameVariables.PvB.getStatus() == PvB_GamePlay.typeOfStatus.ONGOING) {
                    GameVariables.PvB.execute();
                } else {
                    stopped = true;
                }

                if (currentMode == Mode.MENU) {
                    hasChanged = true;
                    GameVariables.PvB = null;
                    this.stop();
                }
            }
        }.start();

        stage.setScene(RenderVariable.scene);
    }

    public static void setupMenuMode(Stage stage) throws IOException {
        hasChanged = false;
        Scene menuScene = RenderVariable.menuScene;

        stage.setTitle("Bomberman");
        Group showStart = (Group) menuScene.lookup("#showStart");
        Group showStart1 = (Group) menuScene.lookup("#showStart1");
        Group joinServer = (Group) menuScene.lookup("#joinServer");
        Group createServer = (Group) menuScene.lookup("#createServer");
        showStart.toFront();
        showStart1.toBack();
        joinServer.toBack();
        createServer.toBack();

        ImageView imgPvB = (ImageView) menuScene.lookup("#playPvB");
        imgPvB.setOnMouseClicked(mouseEvent -> {
            hasChanged = true;
            currentMode = Mode.PvB;
        });


        ImageView imgPvP = (ImageView) menuScene.lookup("#playPvP");
        imgPvP.setOnMouseClicked(mouseEvent -> {
            hasChanged = true;
            currentMode = Mode.PvP;
        });

        ImageView goBackMenu = (ImageView) menuScene.lookup("#backMenu");
        goBackMenu.setOnMouseClicked(mouseEvent -> {
            showStart.toFront();
            showStart1.toBack();
            joinServer.toBack();
            createServer.toBack();

            TextArea IP = (TextArea) menuScene.lookup("#byIP");
            IP.setText("");
        });

        stage.setScene(menuScene);
    }

    public static void setupPvPMode(Stage stage) {
        hasChanged = false;
        Scene menuScene = RenderVariable.menuScene;

        Group showStart = (Group) menuScene.lookup("#showStart");
        Group showStart1 = (Group) menuScene.lookup("#showStart1");
        Group joinServer = (Group) menuScene.lookup("#joinServer");
        Group createServer = (Group) menuScene.lookup("#createServer");
        showStart.toBack();
        showStart1.toFront();
        ImageView imageCreate = (ImageView) menuScene.lookup("#createSV");
        ImageView imageJoin = (ImageView) menuScene.lookup("#joinSV");
        ImageView goBackMenu = (ImageView) menuScene.lookup("#backMenu");

        imageCreate.setDisable(false);
        imageJoin.setDisable(false);
        goBackMenu.setDisable(false);

        imageCreate.setOnMouseClicked(mouseEventNumber1 -> {
            createServer.toFront();
            joinServer.toBack();
            imageCreate.setDisable(true);
            imageJoin.setDisable(true);
            goBackMenu.setDisable(true);

            try {
                TextArea textIP = (TextArea) menuScene.lookup("#textIP");
                textIP.setText(String.valueOf(InetAddress.getLocalHost().getHostAddress()));
            } catch (UnknownHostException event) {
                event.printStackTrace();
            }

            Server.createServer();

            new Thread() {
                public void run() {
                    try {
                        Socket otherSocket = LANVariables.server.serverSocket.accept();
                        new EchoThread(otherSocket).start();
                        hasChanged = true;
                        currentMode = Mode.PvP_IN_EXECUTION;

                        GameVariables.PvP.isWaiting = true;

                        this.stop();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }
                }
            }.start();
        });

        imageJoin.setOnMouseClicked(mouseEventNumber1 -> {
            joinServer.toFront();
            createServer.toBack();
            Button join = (Button) menuScene.lookup("#join");
            join.setOnMouseClicked(mouseEventNumber2 -> {
                //imageCreate.setDisable(true);
                //imageJoin.setDisable(true);
                TextArea IP = (TextArea) menuScene.lookup("#byIP");
                if (IP.getText().equals("")) {
                    return;
                }
                if (!Client.createClientGivenIP(IP.getText())) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("IP invalid!");
                    alert.show();
                }
                else {
                    // kết nối thành công tức là mình là client 2
                    GameVariables.playerRole = GameVariables.role.PLAYER_2;
                    hasChanged = true;
                    currentMode = Mode.PvP_IN_EXECUTION;
                }

            });

        });

        stage.setScene(menuScene);
    }

    public static void executePvPGame(Stage stage) {
        hasChanged = false;

        RenderVariable.scene.setOnKeyPressed(Client::inputPressedKey);
        RenderVariable.scene.setOnKeyReleased(Client::inputReleasedKey);

        Client.countCreatedBomb = 0;

        new AnimationTimer() {
            boolean stopped = false;
            public void handle(long currentNanoTime) {
                // ****** Xử lí chút đặt bomb *********
                if (Client.createdBombThisTurn) {
                    Client.countCreatedBomb ++;
                }

                if (Client.countCreatedBomb >= 5) {
                    Client.createdBombThisTurn = false;
                    Client.countCreatedBomb = 0;
                }

                // **************************************

                if (GameVariables.playerRole == GameVariables.role.PLAYER_1) {
                    if (GameVariables.PvP.getStatus() == PvP_GamePlay.typeOfStatus.ONGOING) {
                        GameVariables.PvP.execute();
                    }
                }

                if (stopped) {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException event) {
                        event.printStackTrace();
                    }
                    currentMode = Mode.MENU;
                }

                LANVariables.client.uploadDataToServer("GET");

                //gọi client nhận dữ liệu và xử lý dữ liệu từ server
                GameVariables.commandListString = LANVariables.client.downloadDataFromServer();

                stopped = Client.executeRenderCommand(GameVariables.commandListString);

                if ((!(stage.isShowing())) || currentMode == Mode.MENU) {
                    SoundVariable.endAllSoundsOnly();

                    try {
                        if(GameVariables.playerRole == GameVariables.role.PLAYER_1) {
                            LANVariables.server.serverSocket.close();
                        }
                        LANVariables.client.socket.close();
                    } catch (IOException event) {
                        event.printStackTrace();
                    }

                    LANVariables.client = null;
                    LANVariables.server = null;
                    GameVariables.playerRole = null;
                    GameVariables.commandList = new JSONArray();
                    GameVariables.temporaryCommandList = new JSONArray();
                    GameVariables.commandListString = new String();
                    GameVariables.PvP = null;
                    hasChanged = true;
                    this.stop();
                }
            }
        }.start();

        stage.setScene(RenderVariable.scene);
    }
}