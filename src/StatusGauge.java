
// package bugi;

public class StatusGauge {

    // ----------------------------------------------------
    // 1. 상태값 (Field)
    // ----------------------------------------------------
    private int hunger;     // 배고픔
    private int health;     // 건강
    private int mood;       // 기분
    private int energy;     // 에너지

    private int maxValue = 100;  // 기본 최대값 (레벨업 시 증가 가능)

    // ----------------------------------------------------
    // 2. 생성자 (기본 게이지 100)
    // ----------------------------------------------------
    public StatusGauge() {
        this.hunger = maxValue;
        this.health = maxValue;
        this.mood = maxValue;
        this.energy = maxValue;
    }

    // ----------------------------------------------------
    // 3. 게이지 감소 (시간 경과)
    // ----------------------------------------------------
    public void decreaseOverTime() {
        decrease("hunger", 2);
        decrease("energy", 2);
        decrease("mood", 2);
        decrease("health", 2);
    }

    // 특정 게이지 이름으로 감소
    private void decrease(String type, int amount) {
        switch (type) {
            case "hunger":
                hunger = Math.max(0, hunger - amount);
                break;
            case "energy":
                energy = Math.max(0, energy - amount);
                break;
            case "mood":
                mood = Math.max(0, mood - amount);
                break;
            case "health":
                health = Math.max(0, health - amount);
                break;
        }
    }

    // ----------------------------------------------------
    // 4. 행동별 상태 변화
    // ----------------------------------------------------
    public void updateStatus(String action) {

        switch (action) {

            case "sleep":
                hunger = decrease(hunger, 8);
                energy = increase(energy, 25);
                mood   = increase(mood, 5);
                break;

            case "study":
                hunger = decrease(hunger, 10);
                health = decrease(health, 8);
                mood   = decrease(mood, 8);
                energy = decrease(energy, 8);
                break;

            case "work":
                hunger = decrease(hunger, 15);
                health = decrease(health, 15);
                mood   = decrease(mood, 10);
                energy = decrease(energy, 12);
                break;

            case "exercise":
                hunger = decrease(hunger, 12);
                health = increase(health, 15);
                mood   = increase(mood, 10);
                energy = decrease(energy, 15);
                break;

            case "wash":
                health = increase(health, 10);
                mood   = increase(mood, 8);
                break;

            case "heal":
                health = increase(health, 40);
                mood   = decrease(mood, 5);
                break;
        }
    }

    // ----------------------------------------------------
    // 5. 게이지 증가/감소 로직
    // ----------------------------------------------------
    private int decrease(int value, int amount) {
        return Math.max(0, value - amount);
    }

    private int increase(int value, int amount) {
        return Math.min(maxValue, value + amount);
    }

    // ----------------------------------------------------
    // 6. 레벨업 시 게이지 최대값 증가
    // ----------------------------------------------------
    public void increaseStats() {
        maxValue += 10;  // 최대값 증가
        hunger = maxValue;
        health = maxValue;
        mood = maxValue;
        energy = maxValue;
    }

    // ----------------------------------------------------
    // 7. Getter
    // ----------------------------------------------------
    public int getHunger() {
        return hunger;
    }

    public int getHealth() {
        return health;
    }

    public int getMood() {
        return mood;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public String toString() {
        return "[Hunger=" + hunger + ", Health=" + health +
                ", Mood=" + mood + ", Energy=" + energy + "]";
    }
    
    // ----------------------------------------------------
    // 8. setter
    // ----------------------------------------------------
    public void setHunger(int value) {
        hunger = Math.max(0, Math.min(value, maxValue));
    }

    public void setHealth(int value) {
        health = Math.max(0, Math.min(value, maxValue));
    }

    public void setMood(int value) {
        mood = Math.max(0, Math.min(value, maxValue));
    }

    public void setEnergy(int value) {
        energy = Math.max(0, Math.min(value, maxValue));
    }

    public boolean isGameOver() {
        int threshold = 5;
        return hunger <= threshold || health <= threshold || mood <= threshold || energy <= threshold;
    }
}

