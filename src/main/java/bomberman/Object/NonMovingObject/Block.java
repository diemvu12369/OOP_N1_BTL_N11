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
    public enum StateOfBlock {
        STARTING_STATE_,
        EXPLODED_STATE_,
        ENDING_STATE_
    }

    /**
     * Trạng thái hiện tại của block.
     */
    protected StateOfBlock blockState;

    // Image object
    protected Image STARTING_STATE_IMAGE = FilesPath.Brick;
    protected Image EXPLODED_STATE_IMAGE = FilesPath.BrickExploded;
    protected Image ENDING_STATE_IMAGE;

    /**
     * Thời gian bắt đầu nổ của block.
     */
    protected long plantTime;

    /**
     * Thời gian bom nổ.
     */
    protected final long EXPLODING_DURATION = 500000000; // 0.5 giây

    public long getExplodeTime() {
        return plantTime;
    }

    public void setExplodeTime(long plantTime) {
        this.plantTime = plantTime;
    }

    public boolean isStartingState() {
        return blockState == StateOfBlock.STARTING_STATE_;
    }

    public boolean isExplodedState() {
        return blockState == StateOfBlock.EXPLODED_STATE_;
    }

    public boolean isEndingState() {
        return blockState == StateOfBlock.ENDING_STATE_;
    }

    public void setStateOfBlock(StateOfBlock blockState) {
        this.blockState = blockState;
    }

    /**
     * Constructor cho block.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Block(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);

        blockState = StateOfBlock.STARTING_STATE_;
    }

    /**
     * Check xem block đã hết thời gian vụ nổ hay chưa.
     *
     * @return chưa hoặc rồi
     */
    public boolean isExplodingExpired() {
        return (isExplodedState() && System.nanoTime() - plantTime >= EXPLODING_DURATION);
    }

    /**
     * Set Image Info for block.
     */
    public abstract void setEndingStateImageInfo();

    @Override
    public Image getImage() {
        setEndingStateImageInfo();

        if (isExplodedState()) {
            return EXPLODED_STATE_IMAGE;
        }

        if (isEndingState()) {
            return ENDING_STATE_IMAGE;
        }

        return STARTING_STATE_IMAGE;
    }
}
