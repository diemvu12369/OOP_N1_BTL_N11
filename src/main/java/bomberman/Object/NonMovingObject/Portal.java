package bomberman.Object.NonMovingObject;

import bomberman.GlobalVariable.FilesPath;
import bomberman.Map.PlayGround;

public class Portal extends Block {
    /**
     * Constructor cho Portal.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Portal(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);
    }

    @Override
    public void setEndingStateImageInfo() {
        ENDING_STATE_IMAGE = FilesPath.Portal;
    }

    @Override
    public void setSettingGraphic() {
        setFramePerSprite(3);
    }
}
