package bomberman.Server_Client;

import java.io.*;
import java.net.*;

import bomberman.GlobalVariable.*;
import bomberman.Object.MovingObject.MovingObject;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.sound.sampled.Clip;

public class Client {

    // địa chỉ máy chủ
    InetAddress host = null;
    // socket
    public Socket socket = null;
    // luồng đẩy dữ liệu đến server
    public BufferedWriter outputStream = null;
    // luồng nhận dữ liệu từ server
    public BufferedReader inputStream = null;

    // khởi tạo đối tượng client mới
    public Client() {
        try {
            host = InetAddress.getLocalHost();
            socket = new Socket(host.getHostName(), LANVariables.PORT);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            socket.setTcpNoDelay(true);
        } catch (UnknownHostException event) {
            socket = null;
        } catch (IOException event) {
            socket = null;
        }
    }

    public Client(String IP) {
        try {
            host = InetAddress.getByName(IP);
            socket = new Socket(host.getHostName(), LANVariables.PORT);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            socket.setTcpNoDelay(true);
        } catch (UnknownHostException event) {
            socket = null;
        } catch (IOException event) {
            socket = null;
        }
    }

    public static boolean createClientGivenIP(String IP) {
        LANVariables.client = new Client(IP);
        if (LANVariables.client == null || LANVariables.client.socket == null) return false;
        GameVariables.playerRole = GameVariables.role.PLAYER_2;
        return true;
    }

    // gửi dữ liệu tới server
    public void uploadDataToServer(JSONObject json) {
        try {
            outputStream.write(json.toString() + "\n");
            outputStream.flush();
        } catch (UnknownHostException event) {
            System.err.println("Trying to connect to unknown host: " + event);
        } catch (IOException event) {
            System.err.println("IOException:  " + event);
        }
    }

    public void uploadDataToServer(String s) {
        try {
            outputStream.write(s + "\n");
            outputStream.flush();
        } catch (UnknownHostException event) {
            System.err.println("Trying to connect to unknown host: " + event);
        } catch (IOException event) {
            System.err.println("IOException:  " + event);
        }
    }

    // nhận data từ server
    public String downloadDataFromServer() {
        try {
            String command = inputStream.readLine();
            if ((command == null) || (command.length() == 0) || (command.charAt(0) != '[')) {
                return "NOT COMMAND";
            } else {
                return command;
            }
        } catch (IOException event) {
            System.out.println("Can not read data from server");
            return "NOT COMMAND";
        }
    }

    /**
     * Dùng để kiểm soát chỉ được gửi 1 lệnh đặt bomb mỗi 30 tick đến server 1 lần.
     * Tránh bị lặp tiếng.
     * True là đã đặt, false là chưa.
     */
    public static boolean createdBombThisTurn = false;

    public static int countCreatedBomb = 0;

    /**
     * Xử lí thao tác ấn phím. Biến nó thành lệnh gửi đến server
     *
     * @param event Key Event
     */
    public static void inputPressedKey(KeyEvent event) {
        JSONObject json = new JSONObject();
        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            try {
                json.put("player", GameVariables.playerRole);
                json.put("direction", MovingObject.DirectionOfObject.RIGHT);
                json.put("status", true);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            try {
                json.put("player", GameVariables.playerRole);
                json.put("direction", MovingObject.DirectionOfObject.LEFT);
                json.put("status", true);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
            try {
                json.put("player", GameVariables.playerRole);
                json.put("direction", MovingObject.DirectionOfObject.UP);
                json.put("status", true);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            try {
                json.put("player", GameVariables.playerRole);
                json.put("direction", MovingObject.DirectionOfObject.DOWN);
                json.put("status", true);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else if (event.getCode() == KeyCode.SPACE) {
            if (!createdBombThisTurn) {
                try {
                    json.put("player", GameVariables.playerRole);
                    json.put("direction", "placeBomb");
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }

                createdBombThisTurn = true;
            }
        } else return;

        // Gửi lệnh đến server
        LANVariables.client.uploadDataToServer(json);
    }

    /**
     * Xử lí thao tác nhả phím. Biến nó thành lệnh gửi tới server
     *
     * @param event Key Event
     */
    public static void inputReleasedKey(KeyEvent event) {
        JSONObject json = new JSONObject();
        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            try {
                json.put("player", GameVariables.playerRole);
                json.put("direction", MovingObject.DirectionOfObject.RIGHT);
                json.put("status", false);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            try {
                json.put("player", GameVariables.playerRole);
                json.put("direction", MovingObject.DirectionOfObject.LEFT);
                json.put("status", false);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
            try {
                json.put("player", GameVariables.playerRole);
                json.put("direction", MovingObject.DirectionOfObject.UP);
                json.put("status", false);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            try {
                json.put("player", GameVariables.playerRole);
                json.put("direction", MovingObject.DirectionOfObject.DOWN);
                json.put("status", false);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        } else return;

        // gửi lệnh đến server
        LANVariables.client.uploadDataToServer(json);
    }

    // giải mã các lệnh in từ server
    public static boolean executeRenderCommand(String command) {
        if (command.equals("NOT COMMAND")) {
            return false;
        }

        try {
            JSONArray commandList = new JSONArray(command);
            for (int i = 0; i < commandList.length(); i++) {
                JSONObject object = (JSONObject) commandList.get(i);

                // **************************** Render ***********************************
                if (object.has("Image")) {
                    if (object.has("player") && object.get("player").equals("PLAYER_1") && GameVariables.playerRole == GameVariables.role.PLAYER_1) {
                        double x = Double.parseDouble((String) object.get("x"));
                        double y = Double.parseDouble((String) object.get("y"));
                        double width = Double.parseDouble((String) object.get("width"));
                        double length = Double.parseDouble((String) object.get("length"));

                        RenderVariable.gc.drawImage(
                                FilesPath.getNameImageBased((String) object.get("Image")),
                                x,
                                y,
                                width,
                                length
                        );

                        return true;
                    } else if (object.has("player") && object.get("player").equals("PLAYER_2") && GameVariables.playerRole == GameVariables.role.PLAYER_2) {
                        double x = Double.parseDouble((String) object.get("x"));
                        double y = Double.parseDouble((String) object.get("y"));
                        double width = Double.parseDouble((String) object.get("width"));
                        double length = Double.parseDouble((String) object.get("length"));

                        RenderVariable.gc.drawImage(
                                FilesPath.getNameImageBased((String) object.get("Image")),
                                x,
                                y,
                                width,
                                length
                        );

                        return true;
                    }

                    if (object.has("player")) {
                        continue;
                    }

                    double imageX = Double.parseDouble((String) object.get("imageX"));
                    double imageY = Double.parseDouble((String) object.get("imageY"));
                    double widthOfImage = Double.parseDouble((String) object.get("widthOfImage"));
                    double lengthOfImage = Double.parseDouble((String) object.get("lengthOfImage"));
                    double x = Double.parseDouble((String) object.get("x"));
                    double y = Double.parseDouble((String) object.get("y"));
                    double width = Double.parseDouble((String) object.get("width"));
                    double length = Double.parseDouble((String) object.get("length"));
                    RenderVariable.gc.drawImage(
                            FilesPath.getNameImageBased((String) object.get("Image")),
                            imageX,
                            imageY,
                            widthOfImage,
                            lengthOfImage,
                            x,
                            y,
                            width,
                            length
                    );
                } else if (object.has("Audio")) {
                    // **************************** Audio ***********************************

                    String audioMode = (String) object.get("Mode");

                    if (audioMode.equals("Play")) {
                        Clip tempSound = FilesPath.getNameClipBased((String) object.get("Audio"));

                        SoundVariable.playSoundOnly(tempSound);
                    } else if (audioMode.equals("Loop")) {
                        Clip tempSound = FilesPath.getNameClipBased((String) object.get("Audio"));

                        int tempTime = Integer.parseInt((String) object.get("Time"));

                        SoundVariable.loopSoundOnly(tempSound, tempTime);
                    } else if (audioMode.equals("EndAllSound")) {
                        SoundVariable.endAllSoundsOnly();
                    }
                }
            }

            return false;
        } catch (JSONException event) {
            return false;
        }
    }
}