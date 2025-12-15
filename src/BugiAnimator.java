
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

    private Animation happyAnim;
    private Animation angryAnim;
    private Animation sickAnim;

    private Animation currentAnimation;
    private int frameIndex = 0;
    private String lastState = "";

    public BugiAnimator(JLabel label, GameManager gameManager) {
        this.label = label;
        this.gameManager = gameManager;

        // Load all animations
        happyAnim = loadAnimation("/img/happy.gif");
        angryAnim = loadAnimation("/img/angry.gif");
        sickAnim = loadAnimation("/img/sick.gif");
        
        // Load default static image
        URL normalUrl = getClass().getResource("/img/bugi.png");
        if (normalUrl != null) {
            this.normalIcon = new ImageIcon(normalUrl);
        }
    }

    private Animation loadAnimation(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("애니메이션 파일을 찾을 수 없습니다: " + path);
                return null;
            }

            ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
            reader.setInput(ImageIO.createImageInputStream(url.openStream()));

            List<BufferedImage> frames = new ArrayList<>();
            List<Integer> delays = new ArrayList<>();
            int numFrames = reader.getNumImages(true);

            for (int i = 0; i < numFrames; i++) {
                frames.add(reader.read(i));
                IIOMetadata metadata = reader.getImageMetadata(i);
                IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());
                IIOMetadataNode graphicsControl = findNode(root, "GraphicControlExtension");
                int delay = (graphicsControl != null) ? Integer.parseInt(graphicsControl.getAttribute("delayTime")) : 10;
                delays.add(delay * 10); // Convert to milliseconds
            }
            return new Animation(frames, delays);

        } catch (IOException e) {
            System.err.println("애니메이션 로딩 중 오류: " + path + ", " + e.getMessage());
            return null;
        }
    }
    
    private IIOMetadataNode findNode(IIOMetadataNode rootNode, String nodeName) {
        for (int i = 0; i < rootNode.getLength(); i++) {
            if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
                return (IIOMetadataNode) rootNode.item(i);
            }
        }
        return null;
    }

    @Override
    public void run() {
        while (true) {
            try {
                StatusGauge gauge = gameManager.getBugi().getGauge();
                String currentState = "normal";

                // Condition order matters: sick is highest priority
                if (gauge.getHealth() <= 20 && sickAnim != null) {
                    currentState = "sick";
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
                    case "happy": currentAnimation = happyAnim; break;
                    case "angry": currentAnimation = angryAnim; break;
                    case "sick":  currentAnimation = sickAnim;  break;
                    default:      currentAnimation = null;      break;
                }
                
                if (currentAnimation != null) {
                    final BufferedImage frame = currentAnimation.frames.get(frameIndex);
                    // Update UI on the Event Dispatch Thread
                    SwingUtilities.invokeLater(() -> label.setIcon(new ImageIcon(frame.getScaledInstance(150, 200, Image.SCALE_SMOOTH))));
                    Thread.sleep(currentAnimation.delays.get(frameIndex));
                    frameIndex = (frameIndex + 1) % currentAnimation.frames.size();
                } else {
                    // Fallback to normal static icon
                    SwingUtilities.invokeLater(() -> {
                        if (normalIcon != null) {
                            Image scaledImage = normalIcon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
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
