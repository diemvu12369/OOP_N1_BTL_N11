package bomberman.Server_Client;

import bomberman.GlobalVariable.GameVariables;
import bomberman.GlobalVariable.LANVariables;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class EchoThread extends Thread {
    protected Socket socket;

    public EchoThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        InputStream inputStream = null;
        BufferedReader bufferedInputStream = null;
        BufferedWriter outputStream = null;

        // khởi tạo luồng vào và ra cho luồng giao tiếp hiện tại
        try {
            socket.setTcpNoDelay(true);
            inputStream = socket.getInputStream();
            bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream));
            outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException event) {
            return;
        }

        String command;
        while (true) {

            // đọc dữ liệu từ client
            try {
                command = bufferedInputStream.readLine();
                if ((command == null) || command.equalsIgnoreCase("QUIT")) {
                    break;
                } else if (command.contains("GET")) {
                    // Nếu là yêu cầu lấy hình ảnh, gửi các lệnh render cho client
                    //System.outputStream.println(socket.toString());
                    if ((GameVariables.commandList != null) && (GameVariables.commandList.length() > 0)) {
                        String s = GameVariables.commandList.toString() + '\n';
                        outputStream.write(s);
                        outputStream.flush();
                    } else {
                        String s = "NO COMMAND\n";
                        outputStream.write(s);
                        outputStream.flush();
                    }
                } else {
                    // Nếu là yêu cầu thao tác nhân vật, xử lý yêu cầu đó
                    GameVariables.PvP.executeRenderCommand(command);
                }
            } catch (SocketException event) {
                if (LANVariables.server.serverSocket.isClosed())
                    return;
            } catch (IOException event) {
                event.printStackTrace();
                return;
            }
        }
    }
}
