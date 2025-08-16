import javax.sound.sampled.*;
import java.io.File;

public class SoundUtils {
    public static void playSound(String filename) {
        try {
            File file = new File(filename);
            if(file.exists()) {
                AudioInputStream audio = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
            } else {
                System.out.println("Sound file not found: " + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}