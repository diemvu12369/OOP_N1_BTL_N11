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
        LEFT_,
        RIGHT_,
        UP_,
        DOWN_,
        CENTER_,
        HORIZONTAL_,
        VERTICAL_
    }

    /**
     * This flame's type.
     */
    private FlameType type;

    /**
     * Flame start time.
     */
    private long startTime;

    /**
     * Flame duration.
     */
    private final long duration = 500000000; // 0.5 giây

    /**
     * Constructor cho flame.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Flame(PlayGround belongTo, double x, double y, double width, double length) {
        super(belongTo, x, y, width, length);
    }

    /**
     * Constructor cho flame.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     * @param type     loại của flame
     */
    public Flame(PlayGround belongTo, double x, double y, double width, double length, FlameType type) {
        super(belongTo, x, y, width, length);

        this.type = type;

        startTime = System.nanoTime();
    }

    /**
     * Xử lý flame nổ vào các ô.
     */
    public static void handleIntersectCell(GameObject cell) {
        if (cell instanceof Item) {
            if (((Item) cell).isInitialState()) {
                ((Item) cell).setBlockState(Block.BlockState.EXPLODING_STATE_);
                ((Item) cell).setStartExplodingTime(System.nanoTime());
            }
        }

        if (cell instanceof Brick) {
            if (((Brick) cell).isInitialState()) {
                ((Brick) cell).setBlockState(Block.BlockState.EXPLODING_STATE_);
                ((Brick) cell).setStartExplodingTime(System.nanoTime());
            }
        }

        if (cell instanceof Portal) {
            if (((Portal) cell).isInitialState()) {
                ((Portal) cell).setBlockState(Block.BlockState.EXPLODING_STATE_);
                ((Portal) cell).setStartExplodingTime(System.nanoTime());
            }
        }
    }

    /**
     * Check xem ô nào bị nổ.
     */
    public void checkIntersectCells() {
        int x1 = GameVariables.calculateCellIndex(this.getX());
        int x2 = GameVariables.calculateCellIndex(this.getX() + this.getWidth() - 1);
        int y1 = GameVariables.calculateCellIndex(this.getY());
        int y2 = GameVariables.calculateCellIndex(this.getY() + this.getLength() - 1);

        for (int i = y1; i <= y2; i++)
            for (int j = x1; j <= x2; j++) {
                handleIntersectCell(this.getBelongTo().getCells(i, j));
            }
    }

    /**
     * Check xem hết thời gian flame chưa.
     *
     * @return chưa hoặc rồi
     */
    public boolean checkExpired() {
        return (System.nanoTime() - startTime >= duration);
    }

    @Override
    public Image getImage() {
        return FilesPath.Flame;
    }

    @Override
    public void setGraphicSetting() {
        setNumberOfFramePerSprite(4);
    }

    public void draw() {
        // Image hiện tại
        Image currentImage = getImage();

        // Tính toán thông tin image hiện tại
        double imageWidth = currentImage.getHeight();
        double imageLength = currentImage.getWidth();

        double spriteSize = imageWidth / 5;

        numberOfSprite = (int) (imageLength / spriteSize);

        // Tính toán currentFrame
        if (gameFrameCount >= (numberOfSprite * numberOfFramePerSprite)) {
            gameFrameCount = gameFrameCount % (numberOfSprite * numberOfFramePerSprite);
        }

        currentSprite = gameFrameCount / numberOfFramePerSprite;

        gameFrameCount++;

        // Render
        setPosRender(0, 0, 0, 0);

        double renderX = currentSprite * spriteSize;
        double renderY;

        switch (type) {
            case UP_:
                renderY = 1;
                break;
            case DOWN_:
                renderY = 2;
                break;
            case LEFT_:
                renderY = 3;
                break;
            case RIGHT_:
                renderY = 4;
                break;
            default:
                renderY = 0;
                break;
        }

        renderY *= spriteSize;

        render(currentImage, renderX, renderY, spriteSize, spriteSize);
    }
}
