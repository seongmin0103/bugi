
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BugiAnimator implements Runnable {

    private static class Animation {
        final List<BufferedImage> frames;
        final List<Integer> delays;

        Animation(List<BufferedImage> frames, List<Integer> delays) {
            this.frames = frames;
            this.delays = delays;
        }
    }

    private JLabel label;
    private GameManager gameManager;
    private ImageIcon normalIcon;
    private ImageIcon stunIcon;
    private ImageIcon dirtyIcon;
    private ImageIcon hungryIcon;

    private Animation happyAnim;
    private Animation angryAnim;
    private Animation sickAnim;
    private Animation sadAnim;

    private Animation currentAnimation;
    private int frameIndex = 0;
    private String lastState = "";

    public BugiAnimator(JLabel label, GameManager gameManager) {
        this.label = label;
        this.gameManager = gameManager;

        // Load all animations from PNG sequences
        happyAnim = loadAnimationFromPngs("happy", 3);
        angryAnim = loadAnimationFromPngs("angry", 3);
        sickAnim = loadAnimationFromPngs("sick", 3);
        sadAnim = loadAnimationFromPngs("sad", 3);
        
        // Load default static image
        URL normalUrl = getClass().getResource("/img/bugi.png");
        if (normalUrl != null) {
            this.normalIcon = new ImageIcon(normalUrl);
        }
        
        URL stunUrl = getClass().getResource("/img/stun.png");
        if (stunUrl != null) {
            this.stunIcon = new ImageIcon(stunUrl);
        }

        URL dirtyUrl = getClass().getResource("/img/dirty.png");
        if (dirtyUrl != null) {
            this.dirtyIcon = new ImageIcon(dirtyUrl);
        }

        URL hungryUrl = getClass().getResource("/img/hungry.png");
        if (hungryUrl != null) {
            this.hungryIcon = new ImageIcon(hungryUrl);
        }
    }

    private Animation loadAnimationFromPngs(String baseName, int frameCount) {
        List<BufferedImage> frames = new ArrayList<>();
        List<Integer> delays = new ArrayList<>();
        try {
            for (int i = 1; i <= frameCount; i++) {
                URL url = getClass().getResource("/img/" + baseName + i + ".png");
                if (url == null) {
                    System.err.println("애니메이션 파일을 찾을 수 없습니다: " + baseName + i + ".png");
                    continue;
                }
                frames.add(ImageIO.read(url));
                delays.add(150); // 각 프레임 간의 지연 시간 (ms)
            }
        } catch (IOException e) {
            System.err.println("PNG 시퀀스 로딩 중 오류: " + baseName + ", " + e.getMessage());
            return null;
        }
        return new Animation(frames, delays);
    }

    @Override
    public void run() {
        while (true) {
            try {
                StatusGauge gauge = gameManager.getBugi().getGauge();
                String currentState = "normal";

                // 상태 우선순위: stun > sad > sick > hungry > dirty > angry > happy
                if (gauge.getHunger() < 10 && gauge.getHealth() < 10 && gauge.getMood() < 10 && gauge.getEnergy() < 10 && stunIcon != null) {
                    currentState = "stun";
                } else if (gauge.getHunger() < 20 && gauge.getHealth() < 20 && gauge.getMood() < 20 && gauge.getEnergy() < 20 && sadAnim != null) {
                    currentState = "sad";
                } else if (gauge.getHealth() <= 20 && sickAnim != null) {
                    currentState = "sick";
                } else if (gauge.getHunger() < 40 && hungryIcon != null) {
                    currentState = "hungry";
                } else if (gauge.getHealth() < 40 && gauge.getMood() < 40 && dirtyIcon != null) {
                    currentState = "dirty";
                } else if (gauge.getHunger() <= 40 && gauge.getHealth() <= 40 && gauge.getMood() <= 40 && gauge.getEnergy() <= 40 && angryAnim != null) {
                    currentState = "angry";
                } else if (gauge.getHunger() >= 60 && gauge.getHealth() >= 60 && gauge.getMood() >= 60 && gauge.getEnergy() >= 60 && happyAnim != null) {
                    currentState = "happy";
                }

                if (!currentState.equals(lastState)) {
                    frameIndex = 0; // Reset animation if state changes
                }
                lastState = currentState;

                switch (currentState) {
                    case "stun":   currentAnimation = null;      break;
                    case "hungry": currentAnimation = null;      break;
                    case "dirty":  currentAnimation = null;      break;
                    case "sad":    currentAnimation = sadAnim;   break;
                    case "happy":  currentAnimation = happyAnim; break;
                    case "angry":  currentAnimation = angryAnim; break;
                    case "sick":   currentAnimation = sickAnim;  break;
                    default:       currentAnimation = null;      break;
                }
                
                if (currentAnimation != null) {
                    final BufferedImage frame = currentAnimation.frames.get(frameIndex);
                    // Update UI on the Event Dispatch Thread
                    SwingUtilities.invokeLater(() -> label.setIcon(new ImageIcon(frame.getScaledInstance(150, 200, Image.SCALE_SMOOTH))));
                    Thread.sleep(currentAnimation.delays.get(frameIndex));
                    frameIndex = (frameIndex + 1) % currentAnimation.frames.size();
                } else {
                    // 정적 이미지 상태 처리 (normal, stun, dirty, hungry)
                    final ImageIcon iconToSet;
                    if ("stun".equals(currentState) && stunIcon != null) {
                        iconToSet = stunIcon;
                    } else if ("dirty".equals(currentState) && dirtyIcon != null) {
                        iconToSet = dirtyIcon;
                    } else if ("hungry".equals(currentState) && hungryIcon != null) {
                        iconToSet = hungryIcon;
                    } else {
                        iconToSet = normalIcon;
                    }

                    SwingUtilities.invokeLater(() -> {
                        if (iconToSet != null) {
                            Image scaledImage = iconToSet.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
                            label.setIcon(new ImageIcon(scaledImage));
                        }
                    });
                    Thread.sleep(500); // Check state every 500ms when static
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
