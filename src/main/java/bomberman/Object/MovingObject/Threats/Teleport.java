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
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     * @param width    chiều rộng
     * @param length   chiều dài
     */
    public Teleport(PlayGround belongTo, double x, double y, double width, double length) {
        super(belongTo, x, y, width, length);
        this.setType(2);
    }

    /**
     * Constructor cho Teleport.
     *
     * @param belongTo tham chiếu tới PlayGround
     * @param x        tọa độ x
     * @param y        tọa độ y
     */
    public Teleport(PlayGround belongTo, double x, double y) {
        super(belongTo, x, y);
        this.setType(2);
    }

    @Override
    public Image getImage() {
        return FilesPath.Teleport;
    }

    @Override
    public void setGraphicSetting() {
        setNumberOfFramePerSprite(6);
    }

    public void die() {
        SoundVariable.playSound(FilesPath.TeleportDieAudio);
    }

    @Override
    public void move() {
        if (this.checkIntersect(this.getBelongTo().getPlayers().get(0))) {
            int cnt = 0;
            Map<Integer, Pair <Integer, Integer> > map = new HashMap<Integer, Pair <Integer, Integer> >();
            for (int i = 0; i < PvB_GamePlay.map.getNumberOfRow(); i++) {
                for (int j = 0; j < PvB_GamePlay.map.getNumberOfColumn(); j++) {
                    if (!this.getBelongTo().isBlockCell(i, j) && !this.getBelongTo().getStateBomb(i, j)) {
                        cnt++;
                        Pair <Integer, Integer> p = new Pair<>(i, j);
                        map.put(cnt, p);
                    }
                }
            }
            double randomDouble = Math.random();
            randomDouble = randomDouble * 1000 + 1;
            int randomInt = (int) randomDouble;
            randomInt = (randomInt % cnt) + 1;
            Pair <Integer, Integer> p = map.get(randomInt);
            this.getBelongTo().getPlayers().get(0).setX((double) p.getValue() * GameVariables.cellLength);
            this.getBelongTo().getPlayers().get(0).setY((double) p.getKey() * GameVariables.cellLength);
        }
        super.move();
    }
}
