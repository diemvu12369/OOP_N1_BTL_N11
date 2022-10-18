package bomberman.Object.NonMovingObject;

import javafx.scene.image.Image;

import bomberman.GlobalVariable.FilesPath;

import bomberman.Object.GameObject;
import bomberman.Map.PlayGround;

/**
 * Block là các vật thể brick hoặc những vật thể xuất hiện sau khi brick phát nổ.
 */
public abstract class Block extends GameObject {
    /**
     * Các trạng thái của block:
     * - Trạng thái ban đầu (trước khi phá hủy).
     * - Trạng thái đang phát nổ.
     * - Trạng thái cuối cùng.
     */
    public enum BlockState {
        INITIAL_STATE_,
        EXPLODING_STATE_,
        FINAL_STATE_
    }

    /**
     * Trạng thái hiện tại của block.
     */
    protected BlockState blockState;

    // Image object
    protected Image INITIAL_STATE_IMAGE = FilesPath.Brick;
    protected Image EXPLODING_STATE_IMAGE = FilesPath.BrickExploded;
    protected Image FINAL_STATE_IMAGE;

    /**
     * Thời gian bắt đầu nổ của block.
     */
    protected long startExplodingTime;

    /**
     * Thời gian bom nổ.
     */
    protected final long EXPLOSION_DURATION = 500000000; // 0.5 giây

    public long getStartExplodingTime() {
        return startExplodingTime;
    }

    public void setStartExplodingTime(long startExplodingTime) {
        this.startExplodingTime = startExplodingTime;
    }

    public boolean isInitialState() {
        return blockState == BlockState.INITIAL_STATE_;
    }

    public boolean isExplodingState() {
        return blockState == BlockState.EXPLODING_STATE_;
    }

    public boolean isFinalState() {
        return blockState == BlockState.FINAL_STATE_;
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    /**
     * Constructor cho block.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Block(PlayGround belongTo, double x, double y, double width, double length) {
        super(belongTo, x, y, width, length);

        blockState = BlockState.INITIAL_STATE_;
    }

    /**
     * Check xem block đã hết thời gian vụ nổ hay chưa.
     *
     * @return chưa hoặc rồi
     */
    public boolean checkExplodingExpired() {
        return (isExplodingState() && System.nanoTime() - startExplodingTime >= EXPLOSION_DURATION);
    }

    /**
     * Set Image Info for block.
     */
    public abstract void setFinalStateImageInfo();

    @Override
    public Image getImage() {
        setFinalStateImageInfo();

        if (isExplodingState()) {
            return EXPLODING_STATE_IMAGE;
        }

        if (isFinalState()) {
            return FINAL_STATE_IMAGE;
        }

        return INITIAL_STATE_IMAGE;
    }
}
