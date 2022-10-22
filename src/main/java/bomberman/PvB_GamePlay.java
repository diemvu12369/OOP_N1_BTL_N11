package bomberman;

import bomberman.GlobalVariable.SoundVariable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

import bomberman.GlobalVariable.FilesPath;
import bomberman.GlobalVariable.RenderVariable;

import bomberman.Object.*;
import bomberman.Map.PlayGround;
import bomberman.Object.MovingObject.Bomber.Bomber;
import bomberman.Object.MovingObject.Threats.Enemy;
import bomberman.Object.MovingObject.MovingObject;
import bomberman.Object.NonMovingObject.*;


public class PvB_GamePlay {
    /**
     * Trạng thái game (đang chơi, thắng, thua).
     */
    public enum typeOfGameStatus {
        PLAYING_,
        WON_,
        LOSE_
    }

    /**
     * Trạng thái game hiện tại.
     */
    private typeOfGameStatus gameStatus;

    public void setGameStatus(typeOfGameStatus inputGameStatus) {
        gameStatus = inputGameStatus;
    }

    public typeOfGameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Map.
     */
    public static PlayGround map;


    /**
     * Player của màn chơi này.
     */
    public Bomber player;

    /**
     * Biến để kiểm soát game chạy hay dừng.
     */
    private boolean needToWait;

    /**
     * Khởi tạo màn chơi PvB.
     * (Khởi tạo các biến phục vụ cho màn chơi)
     */
    public PvB_GamePlay() {
        map = new PlayGround(FilesPath.PVB_MAP_PATH);

        player = map.getPlayers().get(0);

        needToWait = false;
    }

    /**
     * Render screen.
     */
    public void render() {
        map.render();
    }

    public void playPlayGroundAudio() {
        SoundVariable.loopSound(FilesPath.PlayGroundAudio, 1000);
    }

    /**
     * Lên level.
     */
    public void nextLevel() {
        map.setLevel(map.getLevel() + 1);

        if (map.getLevel() >= map.getMaxLevel()) {
            gameWon();

            return;
        }

        RenderVariable.gc.drawImage(FilesPath.LevelUp,
                RenderVariable.SCREEN_LENGTH / 2 - 200, RenderVariable.SCREEN_WIDTH / 2 - 200,
                400, 400);

        needToWait = true;
        SoundVariable.endAllSounds();

        SoundVariable.playSound(FilesPath.LevelUpAudio);

        player = null;

        map.resetPlayers();
        map.resetEnemies();
        map.resetBombs();
        map.resetFlames();

        map.createMapAtLevel();

        player = map.getPlayers().get(0);
    }

    /**
     * Xử lí thua game.
     */
    public void gameOver() {
        RenderVariable.gc.drawImage(FilesPath.YouLose,
                RenderVariable.SCREEN_LENGTH / 2 - 200, RenderVariable.SCREEN_WIDTH / 2 - 200,
                400, 400);

        needToWait = true;
        long startTime = System.nanoTime();
        do {

        } while (System.nanoTime() - startTime <= 750000000);
        SoundVariable.endAllSounds();
        SoundVariable.playSound(FilesPath.YouLoseAudio);
        gameStatus = typeOfGameStatus.LOSE_;
    }

    /**
     * Xử lý thắng game.
     */
    public void gameWon() {
        RenderVariable.gc.drawImage(FilesPath.YouWon,
                RenderVariable.SCREEN_LENGTH / 2 - 200, RenderVariable.SCREEN_WIDTH / 2 - 200,
                400, 400);

        needToWait = true;
        long startTime = System.nanoTime();
        do {

        } while (System.nanoTime() - startTime <= 750000000);
        SoundVariable.endAllSounds();
        SoundVariable.playSound(FilesPath.YouWonAudio);
        gameStatus = typeOfGameStatus.WON_;
    }

    /**
     * Chạy game.
     */
    public void play() {
        if (needToWait) {
            long startTime = System.nanoTime();

            do {

            } while (System.nanoTime() - startTime <= 2000000000);
            SoundVariable.endAllSounds();
            playPlayGroundAudio();
            needToWait = false;
        }

        for (Flame flame : map.getFlames()) {
            // nếu flame chạm nhân vật
            if (flame.checkIntersect(player)) {
                player.dead();
                gameOver();

                return;
            }

            // nếu flame chạm quái
            for (int j = 0; j < map.getEnemies().size(); j++) {
                if (flame.checkIntersect(map.getEnemies().get(j))) {
                    map.getEnemies().get(j).dead();
                    map.removeEnemy(j);

                    j--;
                }
            }
        }

        for (Enemy enemy : map.getEnemies()) {
            // quái chạm nhân vật
            if (enemy.checkIntersect(player) && enemy.getType() == 1) {
                player.dead();
                gameOver();

                return;
            }
        }

        if (player.checkOnPortal() && map.getEnemies().isEmpty()) {
            nextLevel();

            return;
        }

        //cập nhật trạng thái của bản đồ
        for (int i = 0; i < map.getNumberOfRow(); i++) {
            for (int j = 0; j < map.getNumberOfColumn(); j++) {
                GameObject currentCell = map.getCells(i, j);

                //hủy những ô brick đã hết thời gian nổ
                if (currentCell instanceof Brick) {
                    if (((Brick) currentCell).checkExplodingExpired()) {
                        ((Brick) currentCell).setBlockState(Block.BlockState.FINAL_STATE_);
                    }
                }

                //hủy những ô item đã hết thời gian nổ
                if (currentCell instanceof Item) {
                    if (((Item) currentCell).checkExplodingExpired()) {
                        SoundVariable.playSound(FilesPath.ItemAppearsAudio);
                        ((Item) currentCell).setBlockState(Block.BlockState.FINAL_STATE_);
                    }
                }

                //hủy những ô portal đã hết thời gian nổ
                if (currentCell instanceof Portal) {
                    if (((Portal) currentCell).checkExplodingExpired()) {
                        ((Portal) currentCell).setBlockState(Block.BlockState.FINAL_STATE_);
                    }
                }
            }
        }

        //kiểm tra xem bom đã đến lúc nổ chưa, nếu đến thì cho nổ, tạo flame và xóa bom
        for (int i = 0; i < map.getBombs().size(); i++) {
            if (map.getBombs().get(i).checkExplode()) {
                map.getBombs().get(i).detonateBomb();

                map.getBombs().get(i).getOwner().modifyCurrentBomb(-1);

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

                        map.getBombs().get(j).getOwner().modifyCurrentBomb(-1);

                        map.removeBomb(j);

                        j--;
                    }
            }
        }

        for (Enemy enemy : map.getEnemies()) {
            //Quái di chuyển
            enemy.move();
        }

        // Player luôn di chuyển (đứng im tại chỗ tốc độ bằng 0)
        player.move();

        player.checkEatItems();

        render();
    }

    /**
     * Xử lí thao tác ấn phím.
     *
     * @param e Key Event
     */
    public void inputKeyPress(KeyEvent e) {
        if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.RIGHT_, true);
        } else if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.LEFT_, true);
        } else if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.UP_, true);
        } else if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.DOWN_, true);
        } else if (e.getCode() == KeyCode.SPACE) {
            if (player.canPlaceBomb()) {
                player.placeBomb();
            }
        }
    }

    /**
     * Xử lí thao tác nhả phím.
     *
     * @param e Key Event
     */
    public void inputKeyRelease(KeyEvent e) {
        if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.RIGHT_, false);
        } else if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.LEFT_, false);
        } else if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.UP_, false);
        } else if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.DOWN_, false);
        }
    }
}
