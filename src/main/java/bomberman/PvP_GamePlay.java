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
    public enum gameStatusType {
        PLAYING_,
        NOTPLAYING_,
    }

    /**
     * Trạng thái game hiện tại.
     */
    private gameStatusType gameStatus;

    public void setGameStatus(gameStatusType inputGameStatus) {
        gameStatus = inputGameStatus;
    }

    public gameStatusType getGameStatus() {
        return gameStatus;
    }

    /**
     * Map.
     */
    public PlayGround map;

    /**
     * Player thứ nhất, đồng thời là server.
     */
    public Bomber player1;

    /**
     * Player thứ hai
     */
    public Bomber player2;


    /**
     * Biến để kiểm soát game chạy hay dừng.
     */
    public boolean needToWait;

    /**
     * Khởi tạo màn chơi PvP.
     * (Khởi tạo các biến phục vụ cho màn chơi)
     */
    public PvP_GamePlay() {
        map = new PlayGround(FilesPath.PVP_MAP_PATH);

        player1 = map.getPlayers().get(0);
        player2 = map.getPlayers().get(1);

        needToWait = false;
    }

    /**
     * Render screen.
     */
    public void createCommand() {
        map.render();
        GameVariables.commandList = GameVariables.tempCommandList;
        GameVariables.tempCommandList = new JSONArray();
    }

    public void playPlayGroundAudio() {
        SoundVariable.loopSound(FilesPath.PlayGroundAudio, 1000);
    }

    /**
     * Xử lý game player 1 thắng.
     */
    public void gamePlayer1Won() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Image", "YouWon");
            jsonObject.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            jsonObject.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            jsonObject.put("width", "" + 400);
            jsonObject.put("length", "" + 400);
            jsonObject.put("player", "PLAYER_1");
            GameVariables.tempCommandList.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("Image", "YouLose");
            jsonObject.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            jsonObject.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            jsonObject.put("width", "" + 400);
            jsonObject.put("length", "" + 400);
            jsonObject.put("player", "PLAYER_2");
            GameVariables.tempCommandList.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SoundVariable.endAllSounds();

        GameVariables.commandList = GameVariables.tempCommandList;
        gameStatus = gameStatusType.NOTPLAYING_;
    }

    /**
     * Xử lý game player 2 thắng.
     */
    public void gamePlayer2Won() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Image", "YouWon");
            jsonObject.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            jsonObject.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            jsonObject.put("width", "" + 400);
            jsonObject.put("length", "" + 400);
            jsonObject.put("player", "PLAYER_2");
            GameVariables.tempCommandList.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("Image", "YouLose");
            jsonObject.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            jsonObject.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            jsonObject.put("width", "" + 400);
            jsonObject.put("length", "" + 400);
            jsonObject.put("player", "PLAYER_1");
            GameVariables.tempCommandList.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SoundVariable.endAllSounds();

        GameVariables.commandList = GameVariables.tempCommandList;
        gameStatus = gameStatusType.NOTPLAYING_;
    }

    /**
     * Xử lý game hòa.
     */
    public void gameDraw() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Image", "YouDraw");
            jsonObject.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            jsonObject.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            jsonObject.put("width", "" + 400);
            jsonObject.put("length", "" + 400);
            jsonObject.put("player", "PLAYER_1");
            GameVariables.tempCommandList.put(jsonObject);

            jsonObject = new JSONObject();
            jsonObject.put("Image", "YouDraw");
            jsonObject.put("x", "" + (RenderVariable.SCREEN_LENGTH / 2 - 200));
            jsonObject.put("y", "" + (RenderVariable.SCREEN_WIDTH / 2 - 200));
            jsonObject.put("width", "" + 400);
            jsonObject.put("length", "" + 400);
            jsonObject.put("player", "PLAYER_2");
            GameVariables.tempCommandList.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SoundVariable.endAllSounds();

        GameVariables.commandList = GameVariables.tempCommandList;
        gameStatus = gameStatusType.NOTPLAYING_;
    }

    /**
     * Chạy game.
     */
    public void play() {
        if (needToWait) {
            SoundVariable.endAllSounds();

            playPlayGroundAudio();

            needToWait = false;
        }

        boolean player1_die = false;
        boolean player2_die = false;

        for (Flame flame : map.getFlames()) {

            // nếu flame chạm nhân vật 1
            if (flame.checkIntersect(player1)) {
                player1_die = true;
            }

            //nếu flame chạm nhân vật 2
            if (flame.checkIntersect(player2)) {
                player2_die = true;
            }
        }

        if (player1_die && player2_die) {
            gameDraw();
            return;
        } else if (player1_die) {
            gamePlayer2Won();
            return;
        } else if (player2_die) {
            gamePlayer1Won();
            return;
        }

        //cập nhật trạng thái của bản đồ
        for (int i = 0; i < map.getNumberOfRow(); i++) {
            for (int j = 0; j < map.getNumberOfColumn(); j++) {
                GameObject now = map.getCells(i, j);

                //hủy những ô brick đã hết thời gian nổ
                if (now instanceof Brick) {
                    if (((Brick) now).checkExplodingExpired()) {
                        ((Brick) now).setBlockState(Block.BlockState.FINAL_STATE_);
                    }
                }

                //hủy những ô item đã hết thời gian nổ
                if (now instanceof Item) {
                    if (((Item) now).checkExplodingExpired()) {
                        SoundVariable.playSound(FilesPath.ItemAppearsAudio);
                        ((Item) now).setBlockState(Block.BlockState.FINAL_STATE_);
                    }
                }
            }
        }

        //kiểm tra xem bom đã đến lúc nổ chưa, nếu đến thì cho nổ, tạo flame và xóa bom
        for (int i = 0; i < map.getBombs().size(); i++) {
            if (map.getBombs().get(i).checkExplode()) {
                map.getBombs().get(i).detonateBomb();

                map.getBombs().get(i).getOwner().changeCurrentBomb(-1);

                map.removeBomb(i);

                i--;
            } else {
                map.getBombs().get(i).updateUnblockList();
            }
        }

        for (int i = 0; i < map.getFlames().size(); i++) {
            //kiểm tra flame đã hết thời gian chưa, nếu có thì xóa
            if (map.getFlames().get(i).checkExpired()) {
                map.removeFlame(i);

                i--;
            } else {
                // nếu flame chạm bom, kích nổ bom đó luôn
                for (int j = 0; j < map.getBombs().size(); j++)
                    if (map.getFlames().get(i).checkIntersect(map.getBombs().get(j))) {
                        map.getBombs().get(j).detonateBomb();

                        map.getBombs().get(j).getOwner().changeCurrentBomb(-1);

                        map.removeBomb(j);

                        j--;
                    }
            }
        }

        // Player luôn di chuyển (đứng im tại chỗ tốc độ bằng 0)
        player1.move();
        player2.move();

        player1.checkEatItems();
        player2.checkEatItems();

        // Tạo ra các lệnh render
        createCommand();
    }

    // giải mã các lệnh thao tác nhân vật từ client
    public void decodePlayerCommand(String s) {
        if (s == null || s.length() == 0 || s.charAt(0) != '{') return;
        Bomber player;
        try {
            JSONObject command = new JSONObject(s);
            if (command.get("player").equals("PLAYER_1")) {
                player = player1;
            } else {
                player = player2;
            }

            switch ((String) command.get("direction")) {
                case "placeBomb":
                    if (player.canCreateBomb()) {
                        player.createBomb();
                    }
                    break;
                case "RIGHT_":
                    player.setObjectDirection(MovingObject.ObjectDirection.RIGHT_, (boolean) command.get("status"));
                    break;
                case "LEFT_":
                    player.setObjectDirection(MovingObject.ObjectDirection.LEFT_, (boolean) command.get("status"));
                    break;
                case "UP_":
                    player.setObjectDirection(MovingObject.ObjectDirection.UP_, (boolean) command.get("status"));
                    break;
                case "DOWN_":
                    player.setObjectDirection(MovingObject.ObjectDirection.DOWN_, (boolean) command.get("status"));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
