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
        PvPPLAYING,
    }

    static Mode runningMode = Mode.MENU;
    static boolean isChange = true;

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        Group root = RenderVariable.root;
        Canvas canvas = RenderVariable.canvas;
        Scene scene = RenderVariable.scene;

        root.getChildren().add(canvas);
        scene.setRoot(root);

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if (isChange) {
                    if (runningMode == Mode.MENU) {
                        try {
                            setMenuMode(stage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (runningMode == Mode.PvB) {
                        setPvBMode(stage);
                    } else if (runningMode == Mode.PvP) {
                        setPvPMode(stage);
                    } else if (runningMode == Mode.PvPPLAYING) {
                        startPvPGame(stage);
                    }
                }

                if (!stage.isShowing()) {

                    try {
                        if (LANVariables.server!=null && !LANVariables.server.serverSocket.isClosed()) {
                            LANVariables.server.serverSocket.close();
                        }
                        if (LANVariables.client!=null && !LANVariables.client.socket.isClosed()) {
                            LANVariables.client.socket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LANVariables.client=null;
                    LANVariables.server=null;
                    GameVariables.playerRole=null;
                    GameVariables.commandList = new JSONArray();
                    GameVariables.tempCommandList = new JSONArray();
                    GameVariables.commandListString = new String();
                    GameVariables.PvP_Mode=null;
                }
            }
        }.start();

        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - RenderVariable.SCREEN_LENGTH) / 2);
        stage.setY((primScreenBounds.getHeight() - RenderVariable.SCREEN_WIDTH) / 2);
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setPvBMode(Stage stage) {
        isChange = false;
        runningMode = Mode.PvB;

        GameVariables.PvB_Mode = new PvB_GamePlay();

        GameVariables.PvB_Mode.render();
        GameVariables.PvB_Mode.playPlayGroundAudio();

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

        RenderVariable.scene.setOnKeyPressed(GameVariables.PvB_Mode::inputKeyPress);
        RenderVariable.scene.setOnKeyReleased(GameVariables.PvB_Mode::inputKeyRelease);

        GameVariables.PvB_Mode.setGameStatus(PvB_GamePlay.gameStatusType.PLAYING_);
        new AnimationTimer() {
            boolean stopped = false;

            public void handle(long currentNanoTime) {

                if (stopped) {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runningMode = Mode.MENU;
                }

                if (GameVariables.PvB_Mode.getGameStatus() == PvB_GamePlay.gameStatusType.PLAYING_) {
                    GameVariables.PvB_Mode.play();
                } else {
                    stopped = true;
                }

                if (runningMode == Mode.MENU) {
                    isChange = true;
                    GameVariables.PvB_Mode = null;
                    this.stop();
                }
            }
        }.start();

        stage.setScene(RenderVariable.scene);
    }

    public static void setMenuMode(Stage stage) throws IOException {
        isChange = false;
        Scene menuScene = RenderVariable.menuScene;

        stage.setTitle("Bomberman");
        Group showS = (Group) menuScene.lookup("#showStart");
        Group showS1 = (Group) menuScene.lookup("#showStart1");
        Group joinSv = (Group) menuScene.lookup("#joinSv");
        Group createSv = (Group) menuScene.lookup("#createSv");
        showS.toFront();
        showS1.toBack();
        joinSv.toBack();
        createSv.toBack();

        ImageView imgPvB = (ImageView) menuScene.lookup("#playPvB");
        imgPvB.setOnMouseClicked(mouseEvent -> {
            isChange = true;
            runningMode = Mode.PvB;
        });


        ImageView imgPvP = (ImageView) menuScene.lookup("#playPvP");
        imgPvP.setOnMouseClicked(mouseEvent -> {
            isChange = true;
            runningMode = Mode.PvP;
        });

        ImageView backToMenu = (ImageView) menuScene.lookup("#backMenu");
        backToMenu.setOnMouseClicked(mouseEvent -> {
            showS.toFront();
            showS1.toBack();
            joinSv.toBack();
            createSv.toBack();

            TextArea IP = (TextArea) menuScene.lookup("#byIP");
            IP.setText("");
        });

        stage.setScene(menuScene);
    }

    public static void setPvPMode(Stage stage) {
        isChange = false;
        Scene menuScene = RenderVariable.menuScene;

        Group showS = (Group) menuScene.lookup("#showStart");
        Group showS1 = (Group) menuScene.lookup("#showStart1");
        Group joinSv = (Group) menuScene.lookup("#joinSv");
        Group createSv = (Group) menuScene.lookup("#createSv");
        showS.toBack();
        showS1.toFront();
        ImageView imgCreate = (ImageView) menuScene.lookup("#createSV");
        ImageView imgJoin = (ImageView) menuScene.lookup("#joinSV");
        ImageView backToMenu = (ImageView) menuScene.lookup("#backMenu");

        imgCreate.setDisable(false);
        imgJoin.setDisable(false);
        backToMenu.setDisable(false);

        imgCreate.setOnMouseClicked(mouseEvent1 -> {
            createSv.toFront();
            joinSv.toBack();
            imgCreate.setDisable(true);
            imgJoin.setDisable(true);
            backToMenu.setDisable(true);

            try {
                TextArea textIP = (TextArea) menuScene.lookup("#textIP");
                textIP.setText(String.valueOf(InetAddress.getLocalHost().getHostAddress()));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

            Server.createServer();

            new Thread() {
                public void run() {
                    try {
                        Socket socket2 = LANVariables.server.serverSocket.accept();
                        new EchoThread(socket2).start();
                        isChange = true;
                        runningMode = Mode.PvPPLAYING;

                        GameVariables.PvP_Mode.needToWait = true;

                        this.stop();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        });

        imgJoin.setOnMouseClicked(mouseEvent1 -> {
            joinSv.toFront();
            createSv.toBack();
            Button join = (Button) menuScene.lookup("#join");
            join.setOnMouseClicked(mouseEvent2 -> {
                //imgCreate.setDisable(true);
                //imgJoin.setDisable(true);
                TextArea IP = (TextArea) menuScene.lookup("#byIP");
                if (IP.getText().equals("")) {
                    return;
                }
                if (!Client.createClientWithIP(IP.getText())) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("IP invalid!");
                    alert.show();
                }
                else {
                    // kết nối thành công tức là mình là client 2
                    GameVariables.playerRole = GameVariables.role.PLAYER_2;
                    isChange = true;
                    runningMode = Mode.PvPPLAYING;
                }

            });

        });

        stage.setScene(menuScene);
    }

    public static void startPvPGame(Stage stage) {
        isChange = false;

        RenderVariable.scene.setOnKeyPressed(Client::inputKeyPress);
        RenderVariable.scene.setOnKeyReleased(Client::inputKeyRelease);

        Client.countCreateBomb = 0;

        new AnimationTimer() {
            boolean stopped = false;
            public void handle(long currentNanoTime) {
                // ****** Xử lí chút đặt bomb *********
                if (Client.alreadyCreateBombThisTurn) {
                    Client.countCreateBomb ++;
                }

                if (Client.countCreateBomb >= 5) {
                    Client.alreadyCreateBombThisTurn = false;
                    Client.countCreateBomb = 0;
                }

                // **************************************

                if (GameVariables.playerRole == GameVariables.role.PLAYER_1) {
                    if (GameVariables.PvP_Mode.getGameStatus() == PvP_GamePlay.gameStatusType.PLAYING_) {
                        GameVariables.PvP_Mode.play();
                    }
                }

                if (stopped) {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runningMode = Mode.MENU;
                }

                LANVariables.client.sendDataToServer("GET");

                //gọi client nhận dữ liệu và xử lý dữ liệu từ server
                GameVariables.commandListString = LANVariables.client.readDataFromServer();

                stopped = Client.decodeRenderCommand(GameVariables.commandListString);

                if ((!(stage.isShowing())) || runningMode == Mode.MENU) {
                    SoundVariable.endAllSoundsOnly();

                    try {
                        if(GameVariables.playerRole == GameVariables.role.PLAYER_1) {
                            LANVariables.server.serverSocket.close();
                        }
                        LANVariables.client.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    LANVariables.client=null;
                    LANVariables.server=null;
                    GameVariables.playerRole=null;
                    GameVariables.commandList = new JSONArray();
                    GameVariables.tempCommandList = new JSONArray();
                    GameVariables.commandListString = new String();
                    GameVariables.PvP_Mode=null;
                    isChange = true;
                    this.stop();
                }
            }
        }.start();

        stage.setScene(RenderVariable.scene);
    }
}