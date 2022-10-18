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

    public void changeMaxBomb(int value) {
        this.maxBomb += value;
    }

    /**
     * Số ô mà flame do nhân vật tạo ra, ăn buff thì tăng 1.
     */
    private int flameLength = 1;

    public int getFlameLength() {
        return flameLength;
    }

    public void changeFlameLength(int value) {
        this.flameLength += value;
    }

    /**
     * Số lượng bomb đang đặt.
     */
    private int currentBomb = 0;

    public int getCurrentBomb() {
        return currentBomb;
    }

    public void changeCurrentBomb(int value) {
        this.currentBomb += value;
    }

    /**
     * Constructor cho Bomber.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     */
    public Bomber(PlayGround belongTo, double x, double y) {
        super(belongTo, x, y, 35, 35); // Kích thước mặc định

        setSpeed(3);
    }

    /**
     * Constructor cho Bomber.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tạo độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Bomber(PlayGround belongTo, double x, double y, double width, double length) {
        super(belongTo, x, y, width, length);

        setSpeed(3);
    }

    /**
     * Check xem còn bomb để đặt không.
     *
     * @return có hoặc không
     */
    public boolean canCreateBomb() {
        return currentBomb < maxBomb;
    }

    /**
     * Tạo bomb.
     */
    public void createBomb() {
        if (!canCreateBomb()) {
            return;
        }
        SoundVariable.playSound(FilesPath.PlaceBombAudio);
        int tempX = (int) ((GameVariables.calculateCellIndex(this.getX() + this.getWidth() / 2))
                * GameVariables.cellLength);

        int tempY = (int) ((GameVariables.calculateCellIndex(this.getY() + this.getLength() / 2))
                * GameVariables.cellLength);

        currentBomb++;

        this.getBelongTo().addBomb(new Bomb(this.getBelongTo(), tempX, tempY, GameVariables.cellLength, GameVariables.cellLength, this));
    }

    /**
     * Kiểm tra trên phạm vi đang đứng có item không, nếu có thì thực hiện ăn.
     */
    public void checkEatItems() {
        int x1 = GameVariables.calculateCellIndex(this.getX());
        int x2 = GameVariables.calculateCellIndex(this.getX() + this.getWidth() - 1);
        int y1 = GameVariables.calculateCellIndex(this.getY());
        int y2 = GameVariables.calculateCellIndex(this.getY() + this.getLength() - 1);

        for (int i = y1; i <= y2; i++)
            for (int j = x1; j <= x2; j++) {
                GameObject now = this.getBelongTo().getCells(i, j);

                if (!(now instanceof Item)) {
                    continue;
                }

                if (!((Item) now).isFinalState() || ((Item) now).getAteStatus()) {
                    continue;
                }

                switch (((Item) now).getType()) {
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
                ((Item) now).setAteStatus(true);
            }
    }

    /**
     * Kiểm tra player có đang đứng trên portal không.
     *
     * @return có đứng trên hoặc không
     */
    public boolean checkOnPortal() {
        int x1 = GameVariables.calculateCellIndex(this.getX());
        int x2 = GameVariables.calculateCellIndex(this.getX() + this.getWidth() - 1);
        int y1 = GameVariables.calculateCellIndex(this.getY());
        int y2 = GameVariables.calculateCellIndex(this.getY() + this.getLength() - 1);

        for (int i = y1; i <= y2; i++)
            for (int j = x1; j <= x2; j++) {
                GameObject now = this.getBelongTo().getCells(i, j);

                if (!(now instanceof Portal)) {
                    return false;
                }

                if (((Portal) now).isFinalState()) return true;
            }

        return false;
    }

    public void die() {
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
