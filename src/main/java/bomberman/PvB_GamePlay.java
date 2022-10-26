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
    public enum typeOfStatus {
        ONGOING,
        WINNING,
        LOSING
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
    public static PlayGround playground;


    /**
     * Player của màn chơi này.
     */
    public Bomber player;

    /**
     * Biến để kiểm soát game chạy hay dừng.
     */
    private boolean isWaiting;

    /**
     * Khởi tạo màn chơi PvB.
     * (Khởi tạo các biến phục vụ cho màn chơi)
     */
    public PvB_GamePlay() {
        playground = new PlayGround(FilesPath.PVB_MAP_PATH);

        player = playground.getPlayerList().get(0);

        isWaiting = false;
    }

    /**
     * Render screen.
     */
    public void render() {
        playground.render();
    }

    public void playBackgroundAudio() {
        SoundVariable.loopSound(FilesPath.PlayGroundAudio, 1000);
    }

    /**
     * Lên level.
     */
    public void levelUp() {
        playground.setLevel(playground.getLevel() + 1);

        if (playground.getLevel() >= playground.getMaxLevel()) {
            youWon();

            return;
        }

        RenderVariable.gc.drawImage(FilesPath.LevelUp,
                RenderVariable.SCREEN_LENGTH / 2 - 200, RenderVariable.SCREEN_WIDTH / 2 - 200,
                400, 400);

        isWaiting = true;
        SoundVariable.endAllSounds();

        SoundVariable.playSound(FilesPath.LevelUpAudio);

        player = null;

        playground.resetPlayerList();
        playground.resetEnemies();
        playground.resetBombs();
        playground.resetFlames();

        playground.createMapAtLevel();

        player = playground.getPlayerList().get(0);
    }

    /**
     * Xử lí thua game.
     */
    public void youLost() {
        RenderVariable.gc.drawImage(FilesPath.YouLose,
                RenderVariable.SCREEN_LENGTH / 2 - 200, RenderVariable.SCREEN_WIDTH / 2 - 200,
                400, 400);

        isWaiting = true;
        long plantTime = System.nanoTime();
        do {

        } while (System.nanoTime() - plantTime <= 750000000);
        SoundVariable.endAllSounds();
        SoundVariable.playSound(FilesPath.YouLoseAudio);
        status = typeOfStatus.LOSING;
    }

    /**
     * Xử lý thắng game.
     */
    public void youWon() {
        RenderVariable.gc.drawImage(FilesPath.YouWon,
                RenderVariable.SCREEN_LENGTH / 2 - 200, RenderVariable.SCREEN_WIDTH / 2 - 200,
                400, 400);

        isWaiting = true;
        long plantTime = System.nanoTime();
        do {

        } while (System.nanoTime() - plantTime <= 750000000);
        SoundVariable.endAllSounds();
        SoundVariable.playSound(FilesPath.YouWonAudio);
        status = typeOfStatus.WINNING;
    }

    /**
     * Chạy game.
     */
    public void execute() {
        if (isWaiting) {
            long plantTime = System.nanoTime();

            do {

            } while (System.nanoTime() - plantTime <= 2000000000);
            SoundVariable.endAllSounds();
            playBackgroundAudio();
            isWaiting = false;
        }

        for (Flame flame : playground.getFlameList()) {
            // nếu flame chạm nhân vật
            if (flame.isIntersect(player)) {
                player.dead();
                youLost();

                return;
            }

            // nếu flame chạm quái
            for (int j = 0; j < playground.getEnemyList().size(); j++) {
                if (flame.isIntersect(playground.getEnemyList().get(j))) {
                    playground.getEnemyList().get(j).dead();
                    playground.removeEnemy(j);

                    j--;
                }
            }
        }

        for (Enemy enemy : playground.getEnemyList()) {
            // quái chạm nhân vật
            if (enemy.isIntersect(player) && enemy.getType() == 1) {
                player.dead();
                youLost();

                return;
            }
        }

        if (player.isOnPortal() && playground.getEnemyList().isEmpty()) {
            levelUp();

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

                //hủy những ô portal đã hết thời gian nổ
                if (currentCell instanceof Portal) {
                    if (((Portal) currentCell).isExplodingExpired()) {
                        ((Portal) currentCell).setStateOfBlock(Block.StateOfBlock.ENDING_STATE_);
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

        for (Enemy enemy : playground.getEnemyList()) {
            //Quái di chuyển
            enemy.move();
        }

        // Player luôn di chuyển (đứng im tại chỗ tốc độ bằng 0)
        player.move();

        player.checkConsumedItems();

        render();
    }

    /**
     * Xử lí thao tác ấn phím.
     *
     * @param event Key Event
     */
    public void inputPressedKey(KeyEvent event) {
        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.RIGHT, true);
        } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.LEFT, true);
        } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.UP, true);
        } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.DOWN, true);
        } else if (event.getCode() == KeyCode.SPACE) {
            if (player.canPlaceBomb()) {
                player.placeBomb();
            }
        }
    }

    /**
     * Xử lí thao tác nhả phím.
     *
     * @param event Key Event
     */
    public void inputReleasedKey(KeyEvent event) {
        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.RIGHT, false);
        } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.LEFT, false);
        } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.UP, false);
        } else if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S) {
            player.setDirectionOfObject(MovingObject.DirectionOfObject.DOWN, false);
        }
    }
}
