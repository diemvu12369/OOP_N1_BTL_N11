package bomberman.Object.NonMovingObject;

import javafx.scene.image.Image;

import bomberman.GlobalVariable.FilesPath;
import bomberman.GlobalVariable.GameVariables;

import bomberman.Map.PlayGround;

import bomberman.Object.GameObject;

public class Flame extends GameObject {
    /**
     * List of flame type.
     */
    public enum FlameType {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        CENTER,
        HORIZONTAL,
        VERTICAL
    }

    /**
     * This flame's type.
     */
    private FlameType type;

    /**
     * Flame start time.
     */
    private long plantTime;

    /**
     * Flame duration.
     */
    private final long duration = 500000000; // 0.5 giây

    /**
     * Constructor cho flame.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Flame(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);
    }

    /**
     * Constructor cho flame.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     * @param type     loại của flame
     */
    public Flame(PlayGround correspondingPlayGround, double x, double y, double width, double length, FlameType type) {
        super(correspondingPlayGround, x, y, width, length);

        this.type = type;

        plantTime = System.nanoTime();
    }

    /**
     * Xử lý flame nổ vào các ô.
     */
    public static void handleIntersectCell(GameObject cell) {
        if (cell instanceof Item) {
            if (((Item) cell).isStartingState()) {
                ((Item) cell).setStateOfBlock(Block.StateOfBlock.EXPLODED_STATE_);
                ((Item) cell).setExplodeTime(System.nanoTime());
            }
        }

        if (cell instanceof Brick) {
            if (((Brick) cell).isStartingState()) {
                ((Brick) cell).setStateOfBlock(Block.StateOfBlock.EXPLODED_STATE_);
                ((Brick) cell).setExplodeTime(System.nanoTime());
            }
        }

        if (cell instanceof Portal) {
            if (((Portal) cell).isStartingState()) {
                ((Portal) cell).setStateOfBlock(Block.StateOfBlock.EXPLODED_STATE_);
                ((Portal) cell).setExplodeTime(System.nanoTime());
            }
        }
    }

    /**
     * Check xem ô nào bị nổ.
     */
    public void isIntersectCell() {
        int minX = GameVariables.calculateCellIndex(this.getX());
        int maxX = GameVariables.calculateCellIndex(this.getX() + this.getWidth() - 1);
        int minY = GameVariables.calculateCellIndex(this.getY());
        int maxY = GameVariables.calculateCellIndex(this.getY() + this.getLength() - 1);

        for (int i = minY; i <= maxY; i++)
            for (int j = minX; j <= maxX; j++) {
                handleIntersectCell(this.getCorrespondingPlayGround().getCell(i, j));
            }
    }

    /**
     * Check xem hết thời gian flame chưa.
     *
     * @return chưa hoặc rồi
     */
    public boolean hasEnded() {
        return (System.nanoTime() - plantTime >= duration);
    }

    @Override
    public Image getImage() {
        return FilesPath.Flame;
    }

    @Override
    public void setSettingGraphic() {
        setFramePerSprite(4);
    }

    public void draw() {
        // Image hiện tại
        Image displayImage = getImage();

        // Tính toán thông tin image hiện tại
        double widthOfImage = displayImage.getHeight();
        double lengthOfImage = displayImage.getWidth();

        double sizeOfSprite = widthOfImage / 5;

        spriteNumber = (int) (lengthOfImage / sizeOfSprite);

        // Tính toán currentFrame
        if (countGameFrame >= (spriteNumber * framePerSprite)) {
            countGameFrame = countGameFrame % (spriteNumber * framePerSprite);
        }

        processingSprite = countGameFrame / framePerSprite;

        countGameFrame++;

        // Render
        setPosRender(0, 0, 0, 0);

        double renderX = processingSprite * sizeOfSprite;
        double renderY;

        switch (type) {
            case UP:
                renderY = 1;
                break;
            case DOWN:
                renderY = 2;
                break;
            case LEFT:
                renderY = 3;
                break;
            case RIGHT:
                renderY = 4;
                break;
            default:
                renderY = 0;
                break;
        }

        renderY *= sizeOfSprite;

        render(displayImage, renderX, renderY, sizeOfSprite, sizeOfSprite);
    }
}
