package bomberman.Object.NonMovingObject;

import bomberman.GlobalVariable.FilesPath;
import bomberman.Map.PlayGround;

public class Brick extends Block {
    /**
     * Constructor cho Brick.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Brick(PlayGround belongTo, double x, double y, double width, double length) {
        super(belongTo, x, y, width, length);
    }

    @Override
    public void setFinalStateImageInfo() {
        FINAL_STATE_IMAGE = FilesPath.Grass;
    }

    @Override
    public void setGraphicSetting() {
        setNumberOfFramePerSprite(10);
    }
}
