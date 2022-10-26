package bomberman.GlobalVariable;

import org.json.JSONException;
import org.json.JSONObject;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundVariable {
    public static void playSoundOnly(Clip clip) {
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (!RenderVariable.stateSound) {
            volume.setValue(volume.getMinimum());
        }
        else {
            volume.setValue(6);
        }
        clip.start();
        resetSound(clip);
    }

    public static void playSound(Clip clip) {
        if (GameVariables.playerRole == GameVariables.role.PLAYER_1) {
            JSONObject json = new JSONObject();

            try {
                json.put("Audio", FilesPath.getClipName(clip));
                json.put("Mode", "Play");
                GameVariables.temporaryCommandList.put(json);

            } catch (JSONException event) {
                event.printStackTrace();
            }

            return;
        }
        playSoundOnly(clip);
    }

    public static void loopSoundOnly(Clip clip, int time) {
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (!RenderVariable.stateSound) {
            volume.setValue(volume.getMinimum());
        }
        else {
            volume.setValue(6);
        }

        clip.loop(time);
        resetSound(clip);
    }

    public static void loopSound(Clip clip, int time) {
        if (GameVariables.playerRole == GameVariables.role.PLAYER_1) {
            JSONObject json = new JSONObject();

            try {
                json.put("Audio", FilesPath.getClipName(clip));
                json.put("Mode", "Loop");
                json.put("Time", "" + time);
                GameVariables.temporaryCommandList.put(json);
            } catch (JSONException event) {
                event.printStackTrace();
            }

            return;
        }

        loopSoundOnly(clip, time);
    }

    public static void endSound(Clip clip) {
        clip.stop();
        resetSound(clip);
    }

    public static void resetSound(Clip clip) {
        FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        if (!RenderVariable.stateSound) {
            volume.setValue(volume.getMinimum());
        }
        else {
            volume.setValue(6);
        }

        clip.setMicrosecondPosition(0);
    }

    public static void endAllSoundsOnly() {
        endSound(FilesPath.BalloomDieAudio);
        endSound(FilesPath.BomberDieAudio);
        endSound(FilesPath.YouLoseAudio);
        endSound(FilesPath.ExplosionAudio);
        endSound(FilesPath.OnealDieAudio);
        endSound(FilesPath.YouWonAudio);
        endSound(FilesPath.PlayGroundAudio);
        endSound(FilesPath.PowerUpAudio);
        endSound(FilesPath.ExplosionAudio);
        endSound(FilesPath.PlaceBombAudio);
        endSound(FilesPath.LevelUpAudio);
    }

    public static void endAllSounds() {
        if (GameVariables.playerRole == GameVariables.role.PLAYER_1) {
            JSONObject json = new JSONObject();

            try {
                json.put("Audio", "Nothing");
                json.put("Mode", "EndAllSound");
                GameVariables.temporaryCommandList.put(json);
            } catch (JSONException event) {
                event.printStackTrace();
            }

            return;
        }

        endAllSoundsOnly();
    }
}
