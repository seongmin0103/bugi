
// package bugi;

public class LevelSystem {

    // ----------------------------------------------------
    // 1. 속성 (Field)
    // ----------------------------------------------------
    private int level;
    private int requiredStudyCount;  // 레벨업에 필요한 공부량

    // ----------------------------------------------------
    // 2. 생성자
    // ----------------------------------------------------
    public LevelSystem() {
        this.level = 1;
        this.requiredStudyCount = 20;  // 기본 요구 공부량 (원하면 조정 가능)
    }

    // ----------------------------------------------------
    // 3. 레벨 확인 Getter
    // ----------------------------------------------------
    public int getLevel() {
        return level;
    }

    // ----------------------------------------------------
    // 4. 레벨업 조건 체크
    // ----------------------------------------------------
    public boolean checkLevelUp(ImaginaryBugi bugi) {

        StatusGauge gauge = bugi.getGauge();

        // 조건 1: 공부량 충족
        if (bugi.getStudyCount() < requiredStudyCount) {
            return false;
        }

        // 조건 2: 기절 여부 확인
        if (gauge.getHunger() <= 0 || gauge.getEnergy() <= 0) {
            return false;
        }

        // 조건 3: 기분 수치 50 이상 유지
        if (gauge.getMood() < 50) {
            return false;
        }

        // 모든 조건 충족 → 레벨업 가능
        return true;
    }

    // ----------------------------------------------------
    // 5. 레벨업 처리
    // ----------------------------------------------------
    public void increaseStats() {
        level++;
        requiredStudyCount += 10;  // 레벨 올라갈수록 더 많은 공부 요구
        System.out.println("레벨이 " + level + "로 상승했습니다!");
    }
}

