package bomberman.GlobalVariable;

import javafx.scene.image.Image;

import java.net.URISyntaxException;
import javax.sound.sampled.*;

public class FilesPath {
    /**
     * Tạo đối tượng Image mới.
     *
     * @param path đường dẫn của ảnh
     * @return đối tượng Image mới
     */
    private static Image initializeImage(String path) {
        return new Image(String.valueOf(FilesPath.class.getResource(path)));
    }

    public static Clip initializeClip(String filePath) {
        filePath = "/sound/" + filePath;

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(FilesPath.class.getResource(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
        return null;
    }

    // IMAGE FILE PATH
    public static final Image BackGroundGame = initializeImage("/image/background_game.png");

    public static final Image Bomber = initializeImage("/image/bomber.png");

    public static final Image Bomb = initializeImage("/image/bomb.png");

    public static final Image Oneal = initializeImage("/image/oneal.png");
    public static final Image Balloom = initializeImage("/image/balloom.png");
    public static final Image Teleport = initializeImage("/image/teleport.png");

    public static final Image Grass = initializeImage("/image/grass.png");
    public static final Image Wall = initializeImage("/image/wall.png");
    public static final Image Portal = initializeImage("/image/portal.png");
    public static final Image Brick = initializeImage("/image/brick.png");
    public static final Image BrickExploded = initializeImage("/image/brick_exploded.png");

    public static final Image Flame = initializeImage("/image/flame.png");

    public static final Image PowerUpBomb = initializeImage("/image/power_up_bomb.png");
    public static final Image PowerUpFlame = initializeImage("/image/power_up_flame.png");
    public static final Image PowerUpSpeed = initializeImage("/image/power_up_speed.png");

    public static final Image LevelUp = initializeImage("/image/level_up.png");
    public static final Image YouWon = initializeImage("/image/you_won.png");
    public static final Image YouLose = initializeImage("/image/you_lose.png");
    public static final Image YouDraw = initializeImage("/image/you_draw.png");

    public static final Image BackMenu = initializeImage("/image/back_menu.png");
    public static final Image Sound = initializeImage("/image/sound.png");
    public static final Image SoundOff = initializeImage("/image/sound_off.png");

    // MAP FILE PATH
    public static final String PVB_MAP_PATH = "/map/map.txt";
    public static final String PVP_MAP_PATH = "/map/pvp_map.txt";

    // AUDIO CLIP FILE PATH
    public static Clip BomberDieAudio = initializeClip("bomber_die.wav");
    public static Clip BalloomDieAudio = initializeClip("balloom_die.wav");
    public static Clip OnealDieAudio = initializeClip("oneal_die.wav");
    public static Clip TeleportDieAudio = initializeClip("teleport_die.wav");

    public static Clip ExplosionAudio = initializeClip("explosion.wav");
    public static Clip PlaceBombAudio = initializeClip("place_bomb.wav");
    public static Clip ItemAppearsAudio = initializeClip("item_appears.wav");
    public static Clip PowerUpAudio = initializeClip("power_up.wav");

    public static Clip LevelUpAudio = initializeClip("level_up.wav");
    public static Clip YouWonAudio = initializeClip("you_won.wav");
    public static Clip YouLoseAudio = initializeClip("you_lose.wav");

    public static Clip PlayGroundAudio = initializeClip("play_ground.wav");

    public FilesPath() throws URISyntaxException {
    }

    // TODO: optimize this
    public static String getImageName(Image tmp) {
        if (Bomber.equals(tmp)) {
            return "Bomber";
        } else if (Grass.equals(tmp)) {
            return "Grass";
        } else if (Wall.equals(tmp)) {
            return "Wall";
        } else if (Bomb.equals(tmp)) {
            return "Bomb";
        } else if (Portal.equals(tmp)) {
            return "Portal";
        } else if (Brick.equals(tmp)) {
            return "Brick";
        } else if (BrickExploded.equals(tmp)) {
            return "BrickExploded";
        } else if (Flame.equals(tmp)) {
            return "Flame";
        } else if (PowerUpBomb.equals(tmp)) {
            return "PowerUpBomb";
        } else if (PowerUpFlame.equals(tmp)) {
            return "PowerUpFlame";
        } else if (PowerUpSpeed.equals(tmp)) {
            return "PowerUpSpeed";
        } else if (LevelUp.equals(tmp)) {
            return "LevelUp";
        } else if (YouWon.equals(tmp)) {
            return "YouWon";
        } else if (YouLose.equals(tmp)) {
            return "YouLose";
        } else if (YouDraw.equals(tmp)) {
            return "YouDraw";
        } else if (BackGroundGame.equals(tmp)) {
            return "BackGroundGame";
        }

        return "UNKNOWN";
    }

    public static Image getNameImageBased(String name) {
        switch (name) {
            case "Bomber":
                return Bomber;
            case "Grass":
                return Grass;
            case "Wall":
                return Wall;
            case "Bomb":
                return Bomb;
            case "Portal":
                return Portal;
            case "Brick":
                return Brick;
            case "BrickExploded":
                return BrickExploded;

            case "Flame":
                return Flame;

            case "PowerUpBomb":
                return PowerUpBomb;
            case "PowerUpFlame":
                return PowerUpFlame;
            case "PowerUpSpeed":
                return PowerUpSpeed;

            case "YouWon":
                return YouWon;
            case "YouLose":
                return YouLose;
            case "YouDraw":
                return YouDraw;

            case "BackGroundGame":
                return BackGroundGame;
        }

        return Grass;
    }

    public static String getClipName(Clip tmp) {
        if (BomberDieAudio.equals(tmp)) {
            return "BomberDieAudio";
        } else if (BalloomDieAudio.equals(tmp)) {
            return "BalloomDieAudio";
        } else if (OnealDieAudio.equals(tmp)) {
            return "OnealDieAudio";
        } else if (TeleportDieAudio.equals(tmp)) {
            return "TeleportDieAudio";
        } else if (ExplosionAudio.equals(tmp)) {
            return "ExplosionAudio";
        } else if (PlaceBombAudio.equals(tmp)) {
            return "PlaceBombAudio";
        } else if (ItemAppearsAudio.equals(tmp)) {
            return "ItemAppearsAudio";
        } else if (PowerUpAudio.equals(tmp)) {
            return "PowerUpAudio";
        } else if (LevelUpAudio.equals(tmp)) {
            return "LevelUpAudio";
        } else if (YouWonAudio.equals(tmp)) {
            return "YouWonAudio";
        } else if (YouLoseAudio.equals(tmp)) {
            return "YouLoseAudio";
        } else if (PlayGroundAudio.equals(tmp)) {
            return "PlayGroundAudio";
        }

        return "UNKNOWN";
    }

    public static Clip getNameClipBased(String name) {
        switch (name) {
            case "BomberDieAudio":
                return BomberDieAudio;
            case "BalloomDieAudio":
                return BalloomDieAudio;
            case "OnealDieAudio":
                return OnealDieAudio;
            case "TeleportDieAudio":
                return TeleportDieAudio;
            case "ExplosionAudio":
                return ExplosionAudio;
            case "PlaceBombAudio":
                return PlaceBombAudio;
            case "ItemAppearsAudio":
                return ItemAppearsAudio;
            case "PowerUpAudio":
                return PowerUpAudio;
            case "LevelUpAudio":
                return LevelUpAudio;
            case "YouWonAudio":
                return YouWonAudio;
            case "YouLoseAudio":
                return YouLoseAudio;
        }

        return PlayGroundAudio;
    }
}
