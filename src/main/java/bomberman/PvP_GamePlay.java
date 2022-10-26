package bomberman;

import bomberman.GlobalVariable.*;
import bomberman.Map.PlayGround;
import bomberman.Object.GameObject;
import bomberman.Object.MovingObject.Bomber.Bomber;
import bomberman.Object.MovingObject.MovingObject;
import bomberman.Object.NonMovingObject.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PvP_GamePlay {

    /**
     * Trạng thái game (đang chơi, thắng, thua).
     */
    public enum typeOfStatus {
        ONGOING,
        HAVE_NOT_STARTED,
    }

    /**
     * Trạng thái game hiện tại.
     */
    private typeOfStatus status;

    public void setStatus(typeOfStatus inputStatus) {
        status = inputStatus;
    }

    public typeOfStatus getStatus() {
        return status;
    }

    /**
     * Map.
     */
    public PlayGround playground;

    /**
     * Player thứ nhất, đồng thời là server.
     */
    public Bomber hostPlayer;

    /**
     * Player thứ hai
     */
    public Bomber guestPlayer;


    /**
     * Biến để kiểm soát game chạy hay dừng.
     */
    public boolean isWaiting;

    /**
     * Khởi tạo màn chơi PvP.
     * (Khởi tạo các biến phục vụ cho màn chơi)
     */
    public PvP_GamePlay() {
        playground = new PlayGround(FilesPath.PVP_MAP_PATH);

        hostPlayer = playground.getPlayerList().get(0);
        guestPlayer = playground.getPlayerList().get(1);

        isWaiting = false;
    }

    /**
     * Render screen.
     */
    public void sendRenderCommand() {
        playground.render();
        GameVariables.commandList = GameVariables.temporaryCommandList;
        GameVariables.temporaryCommandList = new JSONArray();
    }

    public void playBackgroundAudio() {
        SoundVariable.loopSound(FilesPath.PlayGroundAudio, 1000);
    }

    /**
     * Xử lý game player 1 thắng.
     */
    public void gameHostPlayerWon() {
        try {
            JSONObject json = new JSONObject();
            json.put("Image", "YouWon");
            json.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            json.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            json.put("width", "" + 400);
            json.put("length", "" + 400);
            json.put("player", "PLAYER_1");
            GameVariables.temporaryCommandList.put(json);

            json = new JSONObject();
            json.put("Image", "YouLose");
            json.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            json.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            json.put("width", "" + 400);
            json.put("length", "" + 400);
            json.put("player", "PLAYER_2");
            GameVariables.temporaryCommandList.put(json);
        } catch (JSONException event) {
            event.printStackTrace();
        }

        SoundVariable.endAllSounds();

        GameVariables.commandList = GameVariables.temporaryCommandList;
        status = typeOfStatus.HAVE_NOT_STARTED;
    }

    /**
     * Xử lý game player 2 thắng.
     */
    public void gameGuestPlayerWon() {
        try {
            JSONObject json = new JSONObject();
            json.put("Image", "YouWon");
            json.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            json.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            json.put("width", "" + 400);
            json.put("length", "" + 400);
            json.put("player", "PLAYER_2");
            GameVariables.temporaryCommandList.put(json);

            json = new JSONObject();
            json.put("Image", "YouLose");
            json.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            json.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            json.put("width", "" + 400);
            json.put("length", "" + 400);
            json.put("player", "PLAYER_1");
            GameVariables.temporaryCommandList.put(json);
        } catch (JSONException event) {
            event.printStackTrace();
        }

        SoundVariable.endAllSounds();

        GameVariables.commandList = GameVariables.temporaryCommandList;
        status = typeOfStatus.HAVE_NOT_STARTED;
    }

    /**
     * Xử lý game hòa.
     */
    public void draw() {
        try {
            JSONObject json = new JSONObject();
            json.put("Image", "YouDraw");
            json.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            json.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            json.put("width", "" + 400);
            json.put("length", "" + 400);
            json.put("player", "PLAYER_1");
            GameVariables.temporaryCommandList.put(json);

            json = new JSONObject();
            json.put("Image", "YouDraw");
            json.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            json.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            json.put("width", "" + 400);
            json.put("length", "" + 400);
            json.put("player", "PLAYER_2");
            GameVariables.temporaryCommandList.put(json);
        } catch (JSONException event) {
            event.printStackTrace();
        }

        SoundVariable.endAllSounds();

        GameVariables.commandList = GameVariables.temporaryCommandList;
        status = typeOfStatus.HAVE_NOT_STARTED;
    }

    /**
     * Chạy game.
     */
    public void execute() {
        if (isWaiting) {
            SoundVariable.endAllSounds();

            playBackgroundAudio();

            isWaiting = false;
        }

        boolean hostPlayer_die = false;
        boolean guestPlayer_die = false;

        for (Flame flame : playground.getFlameList()) {

            // nếu flame chạm nhân vật 1
            if (flame.isIntersect(hostPlayer)) {
                hostPlayer_die = true;
            }

            //nếu flame chạm nhân vật 2
            if (flame.isIntersect(guestPlayer)) {
                guestPlayer_die = true;
            }
        }

        if (hostPlayer_die && guestPlayer_die) {
            draw();
            return;
        } else if (hostPlayer_die) {
            gameGuestPlayerWon();
            return;
        } else if (guestPlayer_die) {
            gameHostPlayerWon();
            return;
        }

        //cập nhật trạng thái của bản đồ
        for (int i = 0; i < playground.numberOfRow(); i++) {
            for (int j = 0; j < playground.numberOfColumn(); j++) {
                GameObject currentCell = playground.getCell(i, j);

                //hủy những ô brick đã hết thời gian nổ
                if (currentCell instanceof Brick) {
                    if (((Brick) currentCell).isExplodingExpired()) {
                        ((Brick) currentCell).setStateOfBlock(Block.StateOfBlock.ENDING_STATE_);
                    }
                }

                //hủy những ô item đã hết thời gian nổ
                if (currentCell instanceof Item) {
                    if (((Item) currentCell).isExplodingExpired()) {
                        SoundVariable.playSound(FilesPath.ItemAppearsAudio);
                        ((Item) currentCell).setStateOfBlock(Block.StateOfBlock.ENDING_STATE_);
                    }
                }
            }
        }

        //kiểm tra xem bom đã đến lúc nổ chưa, nếu đến thì cho nổ, tạo flame và xóa bom
        for (int i = 0; i < playground.getBombList().size(); i++) {
            if (playground.getBombList().get(i).checkExplosion()) {
                playground.getBombList().get(i).explodeBomb();

                playground.getBombList().get(i).getBombPlacer().modifyCurrentBomb(-1);

                playground.removeBomb(i);

                i--;
            } else {
                playground.getBombList().get(i).updateExplodedList();
            }
        }

        for (int i = 0; i < playground.getFlameList().size(); i++) {
            //kiểm tra flame đã hết thời gian chưa, nếu có thì xóa
            if (playground.getFlameList().get(i).hasEnded()) {
                playground.deleteFlame(i);

                i--;
            } else {
                // nếu flame chạm bom, kích nổ bom đó luôn
                for (int j = 0; j < playground.getBombList().size(); j++)
                    if (playground.getFlameList().get(i).isIntersect(playground.getBombList().get(j))) {
                        playground.getBombList().get(j).explodeBomb();

                        playground.getBombList().get(j).getBombPlacer().modifyCurrentBomb(-1);

                        playground.removeBomb(j);

                        j--;
                    }
            }
        }

        // Player luôn di chuyển (đứng im tại chỗ tốc độ bằng 0)
        hostPlayer.move();
        guestPlayer.move();

        hostPlayer.checkConsumedItems();
        guestPlayer.checkConsumedItems();

        // Tạo ra các lệnh render
        sendRenderCommand();
    }

    // giải mã các lệnh thao tác nhân vật từ client
    public void executeRenderCommand(String s) {
        if (s == null || s.length() == 0 || s.charAt(0) != '{') return;
        Bomber player;
        try {
            JSONObject command = new JSONObject(s);
            if (command.get("player").equals("PLAYER_1")) {
                player = hostPlayer;
            } else {
                player = guestPlayer;
            }

            switch ((String) command.get("direction")) {
                case "placeBomb":
                    if (player.canPlaceBomb()) {
                        player.placeBomb();
                    }
                    break;
                case "RIGHT":
                    player.setDirectionOfObject(MovingObject.DirectionOfObject.RIGHT, (boolean) command.get("status"));
                    break;
                case "LEFT":
                    player.setDirectionOfObject(MovingObject.DirectionOfObject.LEFT, (boolean) command.get("status"));
                    break;
                case "UP":
                    player.setDirectionOfObject(MovingObject.DirectionOfObject.UP, (boolean) command.get("status"));
                    break;
                case "DOWN":
                    player.setDirectionOfObject(MovingObject.DirectionOfObject.DOWN, (boolean) command.get("status"));
                    break;
            }
        } catch (JSONException event) {
            event.printStackTrace();
        }
    }
}
