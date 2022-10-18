package bomberman.Object.NonMovingObject;

import bomberman.GlobalVariable.FilesPath;
import bomberman.Map.PlayGround;

public class Item extends Block {
    /**
     * List type of item.
     */
    public enum typeOfItems {
        BOMB_ITEM_,
        FLAME_ITEM_,
        SPEED_ITEM_,
    }

    /**
     * This item's type.
     */
    typeOfItems type;

    /**
     * Status if this item has been eaten.
     */
    private boolean ateStatus = false;

    public typeOfItems getType() {
        return type;
    }

    public void setAteStatus(boolean ateStatus) {

        this.ateStatus = ateStatus;
    }

    public boolean getAteStatus() {
        return ateStatus;
    }

    /**
     * Constructor cho Item.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Item(PlayGround belongTo, double x, double y, double width, double length) {
        super(belongTo, x, y, width, length);
    }

    /**
     * Constructor cho Item.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     * @param type     loại item
     */
    public Item(PlayGround belongTo, double x, double y, double width, double length, typeOfItems type) {
        super(belongTo, x, y, width, length);

        this.type = type;
    }

    @Override
    public void setFinalStateImageInfo() {
        if (ateStatus) {
            FINAL_STATE_IMAGE = FilesPath.Grass;
        } else {
            if (type == typeOfItems.BOMB_ITEM_) {
                FINAL_STATE_IMAGE = FilesPath.PowerUpBomb;
            } else if (type == typeOfItems.FLAME_ITEM_) {
                FINAL_STATE_IMAGE = FilesPath.PowerUpFlame;
            } else {
                FINAL_STATE_IMAGE = FilesPath.PowerUpSpeed;
            }
        }
    }

    @Override
    public void setGraphicSetting() {
        setNumberOfFramePerSprite(5);
    }
}
