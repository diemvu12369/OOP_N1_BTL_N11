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
    private static Image createImage(String path) {
        return new Image(String.valueOf(FilesPath.class.getResource(path)));
    }

    public static Clip createClip(String filePath) {
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
    public static final Image BackGroundGame = createImage("/image/background_game.png");

    public static final Image Bomber = createImage("/image/bomber.png");

    public static final Image Bomb = createImage("/image/bomb.png");

    public static final Image Oneal = createImage("/image/oneal.png");
    public static final Image Balloom = createImage("/image/balloom.png");
    public static final Image Teleport = createImage("/image/teleport.png");

    public static final Image Grass = createImage("/image/grass.png");
    public static final Image Wall = createImage("/image/wall.png");
    public static final Image Portal = createImage("/image/portal.png");
    public static final Image Brick = createImage("/image/brick.png");
    public static final Image BrickExploded = createImage("/image/brick_exploded.png");

    public static final Image Flame = createImage("/image/flame.png");

    public static final Image PowerUpBomb = createImage("/image/power_up_bomb.png");
    public static final Image PowerUpFlame = createImage("/image/power_up_flame.png");
    public static final Image PowerUpSpeed = createImage("/image/power_up_speed.png");

    public static final Image LevelUp = createImage("/image/level_up.png");
    public static final Image YouWon = createImage("/image/you_won.png");
    public static final Image YouLose = createImage("/image/you_lose.png");
    public static final Image YouDraw = createImage("/image/you_draw.png");

    public static final Image BackMenu = createImage("/image/back_menu.png");
    public static final Image Sound = createImage("/image/sound.png");
    public static final Image SoundOff = createImage("/image/sound_off.png");

    // MAP FILE PATH
    public static final String PVB_MAP_PATH = "/map/map.txt";
    public static final String PVP_MAP_PATH = "/map/pvp_map.txt";

    // AUDIO CLIP FILE PATH
    public static Clip BomberDieAudio = createClip("bomber_die.wav");
    public static Clip BalloomDieAudio = createClip("balloom_die.wav");
    public static Clip OnealDieAudio = createClip("oneal_die.wav");
    public static Clip TeleportDieAudio = createClip("teleport_die.wav");

    public static Clip ExplosionAudio = createClip("explosion.wav");
    public static Clip PlaceBombAudio = createClip("place_bomb.wav");
    public static Clip ItemAppearsAudio = createClip("item_appears.wav");
    public static Clip PowerUpAudio = createClip("power_up.wav");

    public static Clip LevelUpAudio = createClip("level_up.wav");
    public static Clip YouWonAudio = createClip("you_won.wav");
    public static Clip YouLoseAudio = createClip("you_lose.wav");

    public static Clip PlayGroundAudio = createClip("play_ground.wav");

    public FilesPath() throws URISyntaxException {
    }

    // TODO: optimize this
    public static String encodeImageName(Image tmp) {
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

    public static Image decodeImageName(String name) {
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

    public static String encodeClipName(Clip tmp) {
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

    public static Clip decodeClipName(String name) {
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
