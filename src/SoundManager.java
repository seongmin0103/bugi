import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

public class SoundManager {

    private Clip clip;
    private boolean isPlaying = false;

    public SoundManager(String filePath) {
        try {
            URL soundUrl = getClass().getResource(filePath);
            if (soundUrl == null) {
                System.err.println("사운드 파일을 찾을 수 없습니다: " + filePath);
                return;
            }

            // Use a buffered input stream for better performance, especially from a JAR
            InputStream audioSrc = soundUrl.openStream();
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            clip = AudioSystem.getClip();
            clip.open(audioStream);

        } catch (UnsupportedAudioFileException e) {
            System.err.println("지원되지 않는 오디오 파일 형식입니다: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("오디오 라인을 사용할 수 없습니다: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("오디오 로딩 중 오류 발생: " + e.getMessage());
        }
    }

    public void playBGM() {
        if (clip != null && !clip.isRunning()) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            isPlaying = true;
        }
    }

    public void stopBGM() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            isPlaying = false;
        }
    }

    public void toggleBGM() {
        if (isPlaying) {
            stopBGM();
        } else {
            playBGM();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
