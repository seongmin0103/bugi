
import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class InventoryDialog extends JDialog {

    private GameManager gameManager;
    private JPanel mainPanel; // Panel to hold item list

    public InventoryDialog(Frame owner, GameManager gm) {
        super(owner, "보관함", true); // true for modal
        this.gameManager = gm;

        initUI();
        setSize(350, 500);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        populateItems();

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void populateItems() {
        mainPanel.removeAll(); // Clear existing items

        Collection<ItemQuantity> items = gameManager.getInventory().getItems();

        if (items.isEmpty()) {
            mainPanel.add(new JLabel("보관함이 비어있습니다."));
        } else {
            for (ItemQuantity iq : items) {
                mainPanel.add(createItemPanel(iq));
            }
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createItemPanel(ItemQuantity itemQuantity) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEtchedBorder());

        Item item = itemQuantity.getItem();
        JLabel nameLabel = new JLabel(String.format("%s (수량: %d)", item.getName(), itemQuantity.getQuantity()));
        JButton useButton = new JButton("사용하기");

        useButton.addActionListener(e -> {
            boolean success = gameManager.getInventory().useItem(item, gameManager.getBugi());

            if (success) {
                JOptionPane.showMessageDialog(this,
                        item.getName() + " 사용 완료!",
                        "사용 성공",
                        JOptionPane.INFORMATION_MESSAGE);

                // Refresh the main game frame UI
                if (getOwner() instanceof GameFrame) {
                    ((GameFrame) getOwner()).refreshUI();
                }

                // Refresh this dialog's content
                populateItems();

            } else {
                // This case should ideally not happen if logic is correct
                JOptionPane.showMessageDialog(this,
                        "아이템을 사용할 수 없습니다.",
                        "사용 실패",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(nameLabel);
        panel.add(useButton);

        return panel;
    }
}
