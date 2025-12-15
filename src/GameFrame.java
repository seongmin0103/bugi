
//package bugi;

import java.awt.*;
import java.util.HashMap;
import javax.swing.*;
import java.net.URL;

public class GameFrame extends JFrame {

    private GameManager gm; // 이 클래스는 별도로 정의되어야 합니다.

    private JLabel lblPlace, lblTime, lblSemester, lblSeason, lblCoins, lblQuest, lblAction;
    private JProgressBar barHunger, barHealth, barMood, barEnergy;
    private JLabel backgroundLabel, bugiLabel;
    private JButton btnLeft, btnRight, btnAction, btnInventory, btnShop;
    private JToggleButton btnBGM;
    private SoundManager soundManager;

    private String[] places = {"집", "공원", "욕실", "학교", "카페", "병원"};
    private int placeIndex = 0;

    private HashMap<String, String> bgMap = new HashMap<>();
    private HashMap<String, String> actionIconMap = new HashMap<>();

    public GameFrame() {

        gm = new GameManager();
        soundManager = new SoundManager("/sound/Bugi_BGM.wav");

        // 💡 1. 배경 이미지 경로 수정 완료
        bgMap.put("집", "/img/home.png");
        bgMap.put("공원", "/img/park.png");
        bgMap.put("욕실", "/img/bath.png");
        bgMap.put("학교", "/img/school.png");
        bgMap.put("카페", "/img/cafe.png");
        bgMap.put("병원", "/img/hospital.png");

        // 💡 4. 행동 버튼 아이콘 경로 추가
        actionIconMap.put("욕실", "/img/soap.jpg");
        actionIconMap.put("병원", "/img/pill.jpg");
        actionIconMap.put("학교", "/img/study.jpg");
        actionIconMap.put("카페", "/img/coffee.jpg");
        actionIconMap.put("집", "/img/lamp.png");       // 새로 추가된 아이콘
        actionIconMap.put("공원", "/img/exercise.png"); // 새로 추가된 아이콘


        initUI();
        refreshUI();
    }

    private void initUI() {

        setTitle("상상부기 키우기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =================== 상단 정보 ===================
        JPanel topPanel = new JPanel(new GridLayout(2, 4, 5, 5)); // 8개 컴포넌트를 위한 레이아웃
        topPanel.setBackground(new Color(30, 30, 30));

        lblCoins = makeInfoLabel("코인: 0");
        lblTime = makeInfoLabel("시간: 아침");
        lblSemester = makeInfoLabel("학기: 1");
        lblSeason = makeInfoLabel("계절: 봄");

        barHunger = makeGaugeBar("배부름", topPanel);
        barHealth = makeGaugeBar("건강", topPanel);
        barMood = makeGaugeBar("기분", topPanel);
        barEnergy = makeGaugeBar("에너지", topPanel);

        topPanel.add(lblCoins);
        topPanel.add(lblTime);
        topPanel.add(lblSeason);
        topPanel.add(lblSemester);
        
        add(topPanel, BorderLayout.NORTH);

        // =================== 중앙 배경 & 부기 (JLayeredPane으로 변경) ===================
        JLayeredPane centerPanel = new JLayeredPane();

        backgroundLabel = new JLabel();
        backgroundLabel.setBounds(0, 0, 400, 520);

        // ★★★ 부기 이미지 로딩 및 크기 조정
        bugiLabel = new JLabel();
        // 💡 2. 부기 이미지 경로 수정 완료
        java.net.URL bugiUrl = getClass().getResource("/img/bugi.png");
        
        if (bugiUrl != null) {
            
            try {
                // 1. 원본 이미지 로드
                ImageIcon originalIcon = new ImageIcon(bugiUrl);
                Image originalImage = originalIcon.getImage();

                // 2. bugiLabel의 설정된 크기 (150x200)를 가져옵니다.
                int bugiWidth = 150; 
                int bugiHeight = 200;
                
                // 3. 이미지 크기를 조정합니다.
                Image scaledImage = originalImage.getScaledInstance(
                    bugiWidth, 
                    bugiHeight, 
                    Image.SCALE_SMOOTH // 품질을 고려한 부드러운 조정
                );
                
                // 4. 조정된 이미지로 아이콘을 설정합니다.
                bugiLabel.setIcon(new ImageIcon(scaledImage));
                
            } catch (Exception e) {
                 System.out.println("❌ /img/bugi.png 크기 조정 중 오류 발생: " + e.getMessage());
            }

        } else {
            // 이 메시지가 출력된다면 Classpath 설정이 잘못된 것입니다.
            System.out.println("❌ /img/bugi.png 로딩 실패! (Classpath 경로 확인 요망)"); 
        }

        bugiLabel.setBounds(130, 260, 150, 200);

        // 장소명 + 좌우 이동
        btnLeft = new JButton("◀");
        btnRight = new JButton("▶");
        lblPlace = new JLabel("집", SwingConstants.CENTER);
        lblPlace.setForeground(Color.WHITE);
        lblPlace.setFont(new Font("맑은 고딕", Font.BOLD, 18));

        JPanel placePanel = new JPanel(new BorderLayout());
        placePanel.setOpaque(false);
        placePanel.setBounds(100, 10, 200, 30);
        placePanel.add(btnLeft, BorderLayout.WEST);
        placePanel.add(lblPlace, BorderLayout.CENTER);
        placePanel.add(btnRight, BorderLayout.EAST);
        
        // BGM 버튼 (좌측 상단으로 이동 및 아이콘 설정)
        btnBGM = new JToggleButton();
        btnBGM.setBounds(10, 10, 40, 40);
        btnBGM.setMargin(new Insets(0, 0, 0, 0));
        btnBGM.setBorderPainted(false);
        btnBGM.setContentAreaFilled(false);
        
        btnBGM.addActionListener(e -> {
            SoundManager.playSound("/sound/button_tap.wav");
            soundManager.toggleBGM();
            updateBGMButtonIcon(); // 상태 변경 후 아이콘 업데이트
        });
        
        // 초기 상태에 맞춰 아이콘 설정
        updateBGMButtonIcon();


        // JLayeredPane에 각 컴포넌트를 추가 (숫자가 높을수록 위에 표시됨)
        centerPanel.add(backgroundLabel, Integer.valueOf(0)); // 가장 아래
        centerPanel.add(bugiLabel, Integer.valueOf(1));       // 중간
        centerPanel.add(placePanel, Integer.valueOf(2));      // 장소이동 UI
        centerPanel.add(btnBGM, Integer.valueOf(3));          // BGM 버튼이 가장 위

        add(centerPanel, BorderLayout.CENTER);

        // =================== 하단 버튼 ===================
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        btnInventory = new JButton();
        btnAction = new JButton();
        btnShop = new JButton();

        int btnSize = 80;
        Dimension buttonSize = new Dimension(btnSize, btnSize);
        
        btnInventory.setPreferredSize(buttonSize);
        btnInventory.setMargin(new Insets(0, 0, 0, 0)); // 마진 제거
        btnInventory.setBorderPainted(false); // 테두리 제거
        btnInventory.setContentAreaFilled(false); // 내용 영역 채우기 비활성화

        btnAction.setPreferredSize(buttonSize);
        btnAction.setMargin(new Insets(0, 0, 0, 0)); // 마진 제거
        btnAction.setBorderPainted(false); // 테두리 제거
        btnAction.setContentAreaFilled(false); // 내용 영역 채우기 비활성화

        btnShop.setPreferredSize(buttonSize);
        btnShop.setMargin(new Insets(0, 0, 0, 0)); // 마진 제거
        btnShop.setBorderPainted(false); // 테두리 제거
        btnShop.setContentAreaFilled(false); // 내용 영역 채우기 비활성화

        setButtonIcon(btnInventory, "/img/storage.jpg", btnSize);
        setButtonIcon(btnShop, "/img/store.jpg", btnSize);

        btnPanel.add(btnInventory);
        btnPanel.add(btnAction);
        btnPanel.add(btnShop);

        lblAction = new JLabel("행동: 없음", SwingConstants.CENTER);
        lblQuest = new JLabel("퀘스트: 없음", SwingConstants.CENTER);

        bottomPanel.add(btnPanel, BorderLayout.NORTH);
        bottomPanel.add(lblAction, BorderLayout.CENTER);
        bottomPanel.add(lblQuest, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // =================== 이벤트 ===================
        btnLeft.addActionListener(e -> {
            SoundManager.playSound("/sound/button_tap.wav");
            placeIndex = (placeIndex - 1 + places.length) % places.length;
            gm.movePlaceOnly(places[placeIndex]);
            refreshUI();
        });

        btnRight.addActionListener(e -> {
            SoundManager.playSound("/sound/button_tap.wav");
            placeIndex = (placeIndex + 1) % places.length;
            gm.movePlaceOnly(places[placeIndex]);
            refreshUI();
        });

        btnAction.addActionListener(e -> {
            SoundManager.playSound("/sound/button_tap.wav");
            String result = gm.doCurrentPlaceAction();
            if (result != null) {
                JOptionPane.showMessageDialog(this, result, "행동 실패", JOptionPane.WARNING_MESSAGE);
            }
            refreshUI();

            // 게임 오버 체크
            if (gm.getBugi().getGauge().isGameOver()) {
                JOptionPane.showMessageDialog(this, "게임오버", "GAME OVER", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });

        btnInventory.addActionListener(e -> {
            SoundManager.playSound("/sound/button_tap.wav");
            InventoryDialog inventoryDialog = new InventoryDialog(this, gm);
            inventoryDialog.setVisible(true);
        });
        btnShop.addActionListener(e -> {
            SoundManager.playSound("/sound/button_tap.wav");
            ShopDialog shopDialog = new ShopDialog(this, gm);
            shopDialog.setVisible(true);
        });
    }

    private JLabel makeInfoLabel(String t) {
        JLabel lbl = new JLabel(t);
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private JProgressBar makeGaugeBar(String title, JPanel parent) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JLabel lbl = new JLabel(title);
        lbl.setForeground(Color.WHITE);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(100);
        bar.setStringPainted(true);

        p.add(lbl, BorderLayout.WEST);
        p.add(bar, BorderLayout.CENTER);

        parent.add(p);
        return bar;
    }

    // ================== UI 갱신 ==================
    public void refreshUI() {

        // 이 부분은 기존 GameManager 클래스를 가정합니다.
        ImaginaryBugi bugi = gm.getBugi();
        StatusGauge g = bugi.getGauge();
        TimeSystem t = gm.getTimeSystem();
        Place current = gm.getPlaceManager().getCurrentPlace();
        String currentPlaceName = current.getName();

        lblPlace.setText(currentPlaceName);

        // 행동 버튼 아이콘 업데이트
        String actionIconPath = actionIconMap.get(currentPlaceName);
        if (actionIconPath != null) {
            setButtonIcon(btnAction, actionIconPath, 80);
            btnAction.setText("");
        } else {
            btnAction.setIcon(null);
            btnAction.setText("행동하기");
        }


        // ★★★ 배경 로딩 및 크기 조정
        java.net.URL bgUrl = getClass().getResource(bgMap.get(currentPlaceName));
        if (bgUrl != null) {
            
            try {
                // 1. 원본 이미지 로드
                ImageIcon originalIcon = new ImageIcon(bgUrl);
                Image originalImage = originalIcon.getImage();

                // 2. JLabel의 설정된 크기 (400x520)를 가져옵니다.
                int targetWidth = backgroundLabel.getWidth();
                int targetHeight = backgroundLabel.getHeight();
                
                // 3. 이미지 크기를 조정합니다.
                Image scaledImage = originalImage.getScaledInstance(
                    targetWidth, 
                    targetHeight, 
                    Image.SCALE_SMOOTH 
                );

                // 4. 조정된 이미지로 아이콘을 설정합니다.
                backgroundLabel.setIcon(new ImageIcon(scaledImage));
                
            } catch (Exception e) {
                System.out.println("❌ 배경 이미지 크기 조정 중 오류 발생: " + e.getMessage());
                backgroundLabel.setIcon(null); 
            }
            
        } else {
            System.out.println("❌ 배경 로딩 실패: " + bgMap.get(currentPlaceName) + " (Classpath 확인 필요)");
            backgroundLabel.setIcon(null); 
        }

        barHunger.setValue(g.getHunger());
        barHealth.setValue(g.getHealth());
        barMood.setValue(g.getMood());
        
        
        
        barEnergy.setValue(g.getEnergy());

        lblCoins.setText("코인: " + bugi.getCoins().getBalance());
        lblTime.setText("시간: " + t.getCurrentTime());
        lblSemester.setText("학기: " + t.getSemester());
        lblSeason.setText("계절: " + t.getCurrentSeason());

        lblAction.setText(getActionKorean(current.getAction()));
        lblQuest.setText("퀘스트: " + gm.getTodayQuest().getDescription());
    }

    private String getActionKorean(String a) {
        switch (a) {
            case "sleep": return "잠자기";
            case "study": return "공부하기";
            case "work": return "알바하기";
            case "exercise": return "운동하기";
            case "wash": return "씻기";
            case "heal": return "진료받기";
            default: return "행동 없음";
        }
    }

    // 버튼 아이콘 설정 헬퍼
    private void setButtonIcon(AbstractButton button, String iconPath, int size) {
        URL imageUrl = getClass().getResource(iconPath);
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
            button.setText("");
        } else {
            System.out.println("❌ 버튼 아이콘 로딩 실패: " + iconPath);
            button.setText("이미지 없음");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameFrame().setVisible(true));
    }

    // BGM 버튼 아이콘 업데이트 헬퍼
    private void updateBGMButtonIcon() {
        if (soundManager.isPlaying()) {
            setButtonIcon(btnBGM, "/img/BGMON.png", 40);
            btnBGM.setSelected(true);
        } else {
            setButtonIcon(btnBGM, "/img/BGMOFF.png", 40);
            btnBGM.setSelected(false);
        }
    }
}
