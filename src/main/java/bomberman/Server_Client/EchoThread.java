package bomberman.Server_Client;

import bomberman.GlobalVariable.GameVariables;
import bomberman.GlobalVariable.LANVariables;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class EchoThread extends Thread {
    protected Socket socket;

    public EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {

        InputStream inp = null;
        BufferedReader brinp = null;
        BufferedWriter out = null;

        // khởi tạo luồng vào và ra cho luồng giao tiếp hiện tại
        try {
            socket.setTcpNoDelay(true);
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            return;
        }

        String line;
        while (true) {

            // đọc dữ liệu từ client
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    break;
                } else if (line.contains("GET")) {
                    // Nếu là yêu cầu lấy hình ảnh, gửi các lệnh render cho client
                    //System.out.println(socket.toString());
                    if ((GameVariables.commandList != null) && (GameVariables.commandList.length() > 0)) {
                        String s = GameVariables.commandList.toString() + '\n';
                        out.write(s);
                        out.flush();
                    } else {
                        String s = "NO COMMAND\n";
                        out.write(s);
                        out.flush();
                    }
                } else {
                    // Nếu là yêu cầu thao tác nhân vật, xử lý yêu cầu đó
                    GameVariables.PvP_Mode.decodePlayerCommand(line);
                }
            } catch (SocketException e) {
                if (LANVariables.server.serverSocket.isClosed())
                    return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
