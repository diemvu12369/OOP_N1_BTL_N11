package bomberman.Object.NonMovingObject;

import bomberman.GlobalVariable.FilesPath;
import bomberman.GlobalVariable.RenderVariable;
import bomberman.Map.PlayGround;
import bomberman.Object.GameObject;
import javafx.scene.image.Image;

public class BackGround extends GameObject {
    /**
     * Constructor cho BackGround.
     *
     * @param belongTo tham chiếu tới PlayGround
     */
    public BackGround(PlayGround belongTo) {
        super(belongTo, 0, 0, RenderVariable.SCREEN_LENGTH, RenderVariable.SCREEN_WIDTH);
    }

    @Override
    public Image getImage() {
        return FilesPath.BackGroundGame;
    }

    @Override
    public void setGraphicSetting() {
        setNumberOfFramePerSprite(1);
    }

    @Override
    public void draw() {
        // Render
        setPosRender(0, 0, 0, 0);

        render(getImage(), 0, 0, RenderVariable.SCREEN_LENGTH, RenderVariable.SCREEN_WIDTH);
    }
}
