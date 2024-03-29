package bomberman.Object.NonMovingObject;

import bomberman.Map.PlayGround;
import javafx.scene.image.Image;

import bomberman.GlobalVariable.FilesPath;

import bomberman.Object.GameObject;

import java.util.Random;

public class Wall extends GameObject {
    /**
     * Số loại wall.
     */
    private final int totalWallType = 16;

    /**
     * Loại wall(Loại hình ảnh của wall).
     * Loại 0 là loại default, còn lại là có grafity.
     */
    private int typeOfWall;

    /**
     * Constructor cho Wall.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Wall(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);

        Random generator = new Random();

        typeOfWall = generator.nextInt(2) * (generator.nextInt(totalWallType - 1) + 1);
    }

    @Override
    public Image getImage() {
        return FilesPath.Wall;
    }

    @Override
    public void setSettingGraphic() {
        setFramePerSprite(3);
    }

    @Override
    public void draw() {
        // Image hiện tại
        Image displayImage = getImage();

        // Tính toán thông tin image hiện tại
        double sizeOfSprite = displayImage.getWidth() / totalWallType;

        // Render
        setPosRender(-6, -6, 12, 12);

        render(displayImage, typeOfWall * sizeOfSprite, 0, sizeOfSprite, sizeOfSprite);
    }
}
