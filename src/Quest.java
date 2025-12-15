
// package bugi;

import java.util.Random;

public class Quest {

    // ----------------------------------------------------
    // 1. 속성 (Field)
    // ----------------------------------------------------
    private String description;  // 퀘스트 설명
    private int rewardCoins;     // 코인 보상
    private Item rewardItem;     // 아이템 보상 (없을 수도 있음)
    private boolean isCompleted; // 완료 여부

    // ----------------------------------------------------
    // 2. 생성자 — 코인 보상만 있는 경우
    // ----------------------------------------------------
    public Quest(String description, int rewardCoins) {
        this.description = description;
        this.rewardCoins = rewardCoins;
        this.rewardItem = null;
        this.isCompleted = false;
    }

    // ----------------------------------------------------
    // 3. 생성자 — 아이템 보상이 있는 경우
    // ----------------------------------------------------
    public Quest(String description, Item rewardItem) {
        this.description = description;
        this.rewardCoins = 0;
        this.rewardItem = rewardItem;
        this.isCompleted = false;
    }

    // ----------------------------------------------------
    // 4. 퀘스트 완료 처리
    // ----------------------------------------------------
    public void complete(ImaginaryBugi bugi) {

        if (isCompleted) {
            System.out.println("이미 완료된 퀘스트입니다!");
            return;
        }

        System.out.println("퀘스트 완료: " + description);

        // 코인 보상
        if (rewardCoins > 0) {
            bugi.getCoins().earn(rewardCoins);
        }

        // 아이템 보상
        if (rewardItem != null) {
            System.out.println("아이템 보상 획득: " + rewardItem.getName());
            rewardItem.use(bugi);   // 사용 즉시 적용하거나
            // → 또는 Inventory 시스템 있으면 그쪽으로 이동하면 됨
        }

        isCompleted = true;
    }

    // ----------------------------------------------------
    // 5. 랜덤 퀘스트 생성
    // ----------------------------------------------------
    public static Quest createRandomQuest() {

        Random rand = new Random();
        int r = rand.nextInt(3); // 0~2 랜덤

        switch (r) {
            case 0:
                return new Quest("아르바이트 사장님이 대타 요청!", 15);

            case 1:
                return new Quest("부모님 심부름 부탁이 왔습니다!", 10);

            case 2:
                Item spam = new Item("스팸", 0, 0, 5, 5, 0); // 기분 +5 건강 +5
                return new Quest("옆집 동기가 스팸을 주었습니다!", spam);

            default:
                return new Quest("의문의 행운을 얻었다!", 5);
        }
    }

    // ----------------------------------------------------
    // 6. Getter
    // ----------------------------------------------------
    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "description='" + description + '\'' +
                ", rewardCoins=" + rewardCoins +
                ", rewardItem=" + rewardItem +
                '}';
    }
}
