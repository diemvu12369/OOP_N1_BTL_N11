package bomberman.Object.MovingObject.Threats;

import java.util.concurrent.ThreadLocalRandom;

import bomberman.Map.PlayGround;

import bomberman.Object.MovingObject.MovingObject;
import javafx.scene.image.Image;

public abstract class Enemy extends MovingObject {
    private int type = 1;
    public boolean randomMove = true;
    /**
     * Thời gian di chuyển theo hướng hiện tại
     */
    private final long duration = 250000000; // 0.25 giây

    private long startTime = System.nanoTime();

    /**
     * Constructor cho Enemy.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Enemy(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);

        setSpeed(2);
    }

    /**
     * Constructor cho Enemy.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     */
    public Enemy(PlayGround correspondingPlayGround, double x, double y) {
        super(correspondingPlayGround, x, y, 35, 35); // Kích thước mặc định

        setSpeed(2);
    }

    @Override
    public void move() {
        if (!randomMove) {
            super.move();
            return;
        }
        if (System.nanoTime() - startTime >= duration) {
            boolean headingUp, headingRight, headingDown, headingLeft;

            headingRight = ThreadLocalRandom.current().nextBoolean();

            if (headingRight) {
                headingLeft = false;
            } else {
                headingLeft = ThreadLocalRandom.current().nextBoolean();
            }

            headingUp = ThreadLocalRandom.current().nextBoolean();

            if (!headingUp && !headingRight && !headingLeft) {
                headingDown = true;
            } else if (headingUp) {
                headingDown = false;
            } else {
                headingDown = ThreadLocalRandom.current().nextBoolean();
            }

            setDirectionOfObject(MovingObject.DirectionOfObject.RIGHT_, headingRight);
            setDirectionOfObject(MovingObject.DirectionOfObject.LEFT_, headingLeft);
            setDirectionOfObject(MovingObject.DirectionOfObject.UP_, headingUp);
            setDirectionOfObject(MovingObject.DirectionOfObject.DOWN_, headingDown);

            startTime = System.nanoTime();
        }

        super.move();
    }

    public void dead() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void draw() {
        // Image hiện tại
        Image currentImage = getImage();

        // Tính toán thông tin image hiện tại
        double widthOfImage = currentImage.getHeight();
        double lengthOfImage = currentImage.getWidth();

        double sizeOfSprite = widthOfImage;

        numberOfSprite = (int) (lengthOfImage / sizeOfSprite);

        // Tính toán currentFrame
        if (gameFrameCount >= (numberOfSprite * numberOfFramePerSprite)) {
            gameFrameCount = gameFrameCount % (numberOfSprite * numberOfFramePerSprite);
        }

        currentSprite = gameFrameCount / numberOfFramePerSprite;

        gameFrameCount++;

        // Render
        setPosRender(-5, -10, 10, 10);

        render(currentImage, currentSprite * sizeOfSprite, 0, sizeOfSprite, sizeOfSprite);
    }
}
