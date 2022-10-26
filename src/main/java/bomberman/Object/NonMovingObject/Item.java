package bomberman.Object.NonMovingObject;

import bomberman.GlobalVariable.FilesPath;
import bomberman.Map.PlayGround;

public class Item extends Block {
    /**
     * List type of item.
     */
    public enum typeOfItems {
        ITEM_BOMB_,
        ITEM_FLAME_,
        ITEM_SPEED_,
    }

    /**
     * This item's type.
     */
    typeOfItems type;

    /**
     * Status if this item has been consumed.
     */
    private boolean consumed = false;

    public typeOfItems getType() {
        return type;
    }

    public void setConsumed(boolean consumed) {

        this.consumed = consumed;
    }

    public boolean getConsumed() {
        return consumed;
    }

    /**
     * Constructor cho Item.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Item(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);
    }

    /**
     * Constructor cho Item.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     * @param type     loại item
     */
    public Item(PlayGround correspondingPlayGround, double x, double y, double width, double length, typeOfItems type) {
        super(correspondingPlayGround, x, y, width, length);

        this.type = type;
    }

    @Override
    public void setEndingStateImageInfo() {
        if (consumed) {
            ENDING_STATE_IMAGE = FilesPath.Grass;
        } else {
            if (type == typeOfItems.ITEM_BOMB_) {
                ENDING_STATE_IMAGE = FilesPath.PowerUpBomb;
            } else if (type == typeOfItems.ITEM_FLAME_) {
                ENDING_STATE_IMAGE = FilesPath.PowerUpFlame;
            } else {
                ENDING_STATE_IMAGE = FilesPath.PowerUpSpeed;
            }
        }
    }

    @Override
    public void setSettingGraphic() {
        setFramePerSprite(5);
    }
}
