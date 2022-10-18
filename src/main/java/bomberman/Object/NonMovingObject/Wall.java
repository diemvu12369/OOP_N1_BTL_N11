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
    private final int numberOfWallType = 16;

    /**
     * Loại wall(Loại hình ảnh của wall).
     * Loại 0 là loại default, còn lại là có grafity.
     */
    private int wallType;

    /**
     * Constructor cho Wall.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Wall(PlayGround belongTo, double x, double y, double width, double length) {
        super(belongTo, x, y, width, length);

        Random generator = new Random();

        wallType = generator.nextInt(2) * (generator.nextInt(numberOfWallType - 1) + 1);
    }

    @Override
    public Image getImage() {
        return FilesPath.Wall;
    }

    @Override
    public void setGraphicSetting() {
        setNumberOfFramePerSprite(3);
    }

    @Override
    public void draw() {
        // Image hiện tại
        Image currentImage = getImage();

        // Tính toán thông tin image hiện tại
        double spriteSize = currentImage.getWidth() / numberOfWallType;

        // Render
        setPosRender(-6, -6, 12, 12);

        render(currentImage, wallType * spriteSize, 0, spriteSize, spriteSize);
    }
}
