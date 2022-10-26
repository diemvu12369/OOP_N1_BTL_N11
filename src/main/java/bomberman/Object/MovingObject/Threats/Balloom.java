package bomberman.Object.MovingObject.Threats;

import bomberman.GlobalVariable.FilesPath;
import bomberman.GlobalVariable.SoundVariable;
import bomberman.Map.PlayGround;
import javafx.scene.image.Image;


public class Balloom extends Enemy {
    /**
     * Constructor cho Balloom.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Balloom(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);
    }

    /**
     * Constructor cho Balloom.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     */
    public Balloom(PlayGround correspondingPlayGround, double x, double y) {
        super(correspondingPlayGround, x, y);
    }

    @Override
    public Image getImage() {
        return FilesPath.Balloom;
    }

    @Override
    public void setSettingGraphic() {
        setFramePerSprite(6);
    }

    public void dead() {
        SoundVariable.playSound(FilesPath.BalloomDieAudio);
    }
}
