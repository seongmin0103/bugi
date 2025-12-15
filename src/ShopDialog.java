
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ShopDialog extends JDialog {

    private GameManager gameManager;

    public ShopDialog(Frame owner, GameManager gm) {
        super(owner, "상점", true); // true for modal
        this.gameManager = gm;

        initUI();
        setSize(350, 500);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        // Main panel with a scrollable list of items
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        ArrayList<Item> items = gameManager.getItemManager().getAvailableItems();

        for (Item item : items) {
            mainPanel.add(createItemPanel(item));
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> {
            SoundManager.playSound("/sound/button_tap.wav");
            dispose();
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createItemPanel(Item item) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEtchedBorder());

        JLabel nameLabel = new JLabel(String.format("%s (가격: %d 코인)", item.getName(), item.getPrice()));
        JButton buyButton = new JButton("구매");

        buyButton.addActionListener(e -> {
            SoundManager.playSound("/sound/button_tap.wav");
            boolean success = gameManager.purchaseItemToInventory(item);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        item.getName() + " 구매 완료!",
                        "구매 성공",
                        JOptionPane.INFORMATION_MESSAGE);

                // Refresh the main game frame UI
                if (getOwner() instanceof GameFrame) {
                    ((GameFrame) getOwner()).refreshUI();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "코인이 부족합니다.",
                        "구매 실패",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(nameLabel);
        panel.add(buyButton);

        return panel;
    }
}
