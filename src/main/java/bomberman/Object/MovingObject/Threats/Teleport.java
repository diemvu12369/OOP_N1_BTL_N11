package bomberman.Object.MovingObject.Threats;

import bomberman.GlobalVariable.FilesPath;
import bomberman.GlobalVariable.GameVariables;
import bomberman.GlobalVariable.SoundVariable;
import bomberman.Map.PlayGround;
import bomberman.PvB_GamePlay;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Teleport extends Enemy {
    /**
     * Constructor cho Teleport.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Teleport(PlayGround correspondingPlayGround, double x, double y, double width, double length) {
        super(correspondingPlayGround, x, y, width, length);
        this.setType(2);
    }

    /**
     * Constructor cho Teleport.
     *
     * @param correspondingPlayGround tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     */
    public Teleport(PlayGround correspondingPlayGround, double x, double y) {
        super(correspondingPlayGround, x, y);
        this.setType(2);
    }

    @Override
    public Image getImage() {
        return FilesPath.Teleport;
    }

    @Override
    public void setSettingGraphic() {
        setFramePerSprite(6);
    }

    public void dead() {
        SoundVariable.playSound(FilesPath.TeleportDieAudio);
    }

    @Override
    public void move() {
        if (this.isIntersect(this.getCorrespondingPlayGround().getPlayerList().get(0))) {
            int cnt = 0;
            Map<Integer, Pair <Integer, Integer> > playground = new HashMap<Integer, Pair <Integer, Integer> >();
            for (int i = 0; i < PvB_GamePlay.playground.numberOfRow(); i++) {
                for (int j = 0; j < PvB_GamePlay.playground.numberOfColumn(); j++) {
                    if (!this.getCorrespondingPlayGround().isCellBlocked(i, j) && !this.getCorrespondingPlayGround().getBombState(i, j)) {
                        cnt++;
                        Pair <Integer, Integer> p = new Pair<>(i, j);
                        playground.put(cnt, p);
                    }
                }
            }
            double randomlyDouble = Math.random();
            randomlyDouble = randomlyDouble * 1000 + 1;
            int randomlyInt = (int) randomlyDouble;
            randomlyInt = (randomlyInt % cnt) + 1;
            Pair <Integer, Integer> p = playground.get(randomlyInt);
            this.getCorrespondingPlayGround().getPlayerList().get(0).setX((double) p.getValue() * GameVariables.unitLength);
            this.getCorrespondingPlayGround().getPlayerList().get(0).setY((double) p.getKey() * GameVariables.unitLength);
        }
        super.move();
    }
}
