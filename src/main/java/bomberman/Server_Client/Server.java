package bomberman.Server_Client;

import bomberman.GlobalVariable.GameVariables;
import bomberman.GlobalVariable.LANVariables;
import bomberman.PvP_GamePlay;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public ServerSocket serverSocket;

    public Server() {
        try {
            serverSocket = new ServerSocket(LANVariables.PORT);
        } catch (IOException event) {
            event.printStackTrace();
        }
    }

    public static void createServer() {
        Socket socket = null;

        //tạo server mới ở máy mình
        LANVariables.server = new Server();

        // khởi tạo bản thân như client 1
        LANVariables.client = new Client();

        // tạo luồng giao tiếp giữa server và client 1
        try {
            socket = LANVariables.server.serverSocket.accept();
        } catch (IOException event) {
            System.out.println("I/O error: " + event);
        }
        new EchoThread(socket).start();

        GameVariables.PvP = new PvP_GamePlay();
        GameVariables.PvP.execute();
        GameVariables.playerRole = GameVariables.role.PLAYER_1;
        GameVariables.PvP.setStatus(PvP_GamePlay.typeOfStatus.ONGOING);
    }
}