package bomberman.Object.NonMovingObject;

import bomberman.Map.PlayGround;
import javafx.scene.image.Image;

import bomberman.GlobalVariable.FilesPath;

import bomberman.Object.GameObject;

public class Grass extends GameObject {
    /**
     * Constructor cho Grass.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Grass(PlayGround belongTo, double x, double y, double width, double length) {
        super(belongTo, x, y, width, length);
    }

    @Override
    public Image getImage() {
        return FilesPath.Grass;
    }

    @Override
    public void setGraphicSetting() {
        setNumberOfFramePerSprite(3);
    }
}
