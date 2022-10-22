package bomberman.Object.MovingObject.Bomber;

import bomberman.GlobalVariable.FilesPath;
import bomberman.GlobalVariable.GameVariables;

import bomberman.GlobalVariable.SoundVariable;
import bomberman.Map.PlayGround;

import bomberman.Object.GameObject;
import bomberman.Object.MovingObject.MovingObject;
import bomberman.Object.NonMovingObject.Bomb;
import bomberman.Object.NonMovingObject.Item;
import bomberman.Object.NonMovingObject.Portal;
import javafx.scene.image.Image;


public class Bomber extends MovingObject {
    /**
     * Số bomb có thể đặt được.
     */
    private int maxBomb = 1;

    public int getMaxBomb() {
        return maxBomb;
    }

    public void modifyMaxBomb(int value) {
        this.maxBomb += value;
    }

    /**
     * Số ô mà flame do nhân vật tạo ra, ăn buff thì tăng 1.
     */
    private int flameLength = 1;

    public int getFlameLength() {
        return flameLength;
    }

    public void modifyFlameLength(int value) {
        this.flameLength += value;
    }

    /**
     * Số lượng bomb đang đặt.
     */
    private int currentBomb = 0;

    public int getCurrentBomb() {
        return currentBomb;
    }

    public void modifyCurrentBomb(int value) {
        this.currentBomb += value;
    }

    /**
     * Constructor cho Bomber.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     */
    public Bomber(PlayGround correspondingPlayGround, double x, double y) {
        super(correspondingPlayGround, x, y, 35, 35); // Kích thước mặc định

        setSpeed(3);
    }

    /**
     * Constructor cho Bomber.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tạo độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Bomber(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);

        setSpeed(3);
    }

    /**
     * Check xem còn bomb để đặt không.
     *
     * @return có hoặc không
     */
    public boolean canPlaceBomb() {
        return currentBomb < maxBomb;
    }

    /**
     * Tạo bomb.
     */
    public void placeBomb() {
        if (!canPlaceBomb()) {
            return;
        }
        SoundVariable.playSound(FilesPath.PlaceBombAudio);
        int tempX = (int) ((GameVariables.calculateCellIndex(this.getX() + this.getWidth() / 2))
                * GameVariables.unitLength);

        int tempY = (int) ((GameVariables.calculateCellIndex(this.getY() + this.getLength() / 2))
                * GameVariables.unitLength);

        currentBomb++;

        this.getCorrespondingPlayGround().addBomb(new Bomb(this.getCorrespondingPlayGround(), tempX, tempY, GameVariables.unitLength, GameVariables.unitLength, this));
    }

    /**
     * Kiểm tra trên phạm vi đang đứng có item không, nếu có thì thực hiện ăn.
     */
    public void checkEatItems() {
        int minX = GameVariables.calculateCellIndex(this.getX());
        int maxX = GameVariables.calculateCellIndex(this.getX() + this.getWidth() - 1);
        int minY = GameVariables.calculateCellIndex(this.getY());
        int maxY = GameVariables.calculateCellIndex(this.getY() + this.getLength() - 1);

        for (int i = minY; i <= maxY; i++)
            for (int j = minX; j <= maxX; j++) {
                GameObject currentCell = this.getCorrespondingPlayGround().getCells(i, j);

                if (!(currentCell instanceof Item)) {
                    continue;
                }

                if (!((Item) currentCell).isFinalState() || ((Item) currentCell).getAteStatus()) {
                    continue;
                }

                switch (((Item) currentCell).getType()) {
                    case BOMB_ITEM_:
                        maxBomb++;
                        break;
                    case SPEED_ITEM_:
                        setSpeed(getSpeed() + 1);
                        break;
                    case FLAME_ITEM_:
                        flameLength++;
                        break;
                }

                SoundVariable.playSound(FilesPath.PowerUpAudio);
                ((Item) currentCell).setAteStatus(true);
            }
    }

    /**
     * Kiểm tra player có đang đứng trên portal không.
     *
     * @return có đứng trên hoặc không
     */
    public boolean checkOnPortal() {
        int minX = GameVariables.calculateCellIndex(this.getX());
        int maxX = GameVariables.calculateCellIndex(this.getX() + this.getWidth() - 1);
        int minY = GameVariables.calculateCellIndex(this.getY());
        int maxY = GameVariables.calculateCellIndex(this.getY() + this.getLength() - 1);

        for (int i = minY; i <= maxY; i++)
            for (int j = minX; j <= maxX; j++) {
                GameObject currentCell = this.getCorrespondingPlayGround().getCells(i, j);

                if (!(currentCell instanceof Portal)) {
                    return false;
                }

                if (((Portal) currentCell).isFinalState()) return true;
            }

        return false;
    }

    public void dead() {
        SoundVariable.playSound(FilesPath.BomberDieAudio);
    }

    @Override
    public Image getImage() {
        return FilesPath.Bomber;
    }

    @Override
    public void setGraphicSetting() {
        setNumberOfFramePerSprite(4);
    }

    @Override
    protected void render(Image currentImage, double renderX, double renderY, double renderWidth, double renderLength) {
        setPosRender(-15, -25, 30, 30);

        super.render(currentImage, renderX, renderY, renderWidth, renderLength);
    }
}
