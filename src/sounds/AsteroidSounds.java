package sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class AsteroidSounds
{
    /**
     * Gets the sound clip stored at the given path
     * @param path  the path to the sound clip
     * @return      the sound clip
     */
    private static Clip getClip(String path) {
        // Opening the sound file this way will work no matter how the
        // project is exported. The only restriction is that the
        // sound files must be stored in a package.
        try (BufferedInputStream sound = new BufferedInputStream(AsteroidSounds.class.getResourceAsStream(path)))
        {
            // Create and return a Clip that will play a sound file. There are
            // various reasons that the creation attempt could fail. If it
            // fails, return null.
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            return clip;
        }
        catch (LineUnavailableException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        catch (UnsupportedAudioFileException e)
        {
            return null;
        }
    }

    public static final Clip BANG_ALIEN_SHIP = getClip("/sounds/bangAlienShip.wav");
    public static final Clip BANG_LARGE = getClip("/sounds/bangLarge.wav");
    public static final Clip BANG_MEDIUM = getClip("/sounds/bangMedium.wav");
    public static final Clip BANG_SMALL = getClip("/sounds/bangSmall.wav");
    public static final Clip BANG_SHIP = getClip("/sounds/bangShip.wav");
    public static final Clip BEAT_1 = getClip("/sounds/beat1.wav");
    public static final Clip BEAT_2 = getClip("/sounds/beat2.wav");
    public static final Clip FIRE = getClip("/sounds/fire.wav");
    public static final Clip SAUCER_BIG = getClip("/sounds/saucerBig.wav");
    public static final Clip SAUCER_SMALL = getClip("/sounds/saucerSmall.wav");
    public static final Clip THRUST = getClip("/sounds/thrust.wav");
    
    public static void playSound(Clip clip) {
        clip.setFramePosition(0);
        clip.start();
    }
}
