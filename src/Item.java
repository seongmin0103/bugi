
// package bugi;

public class Item {

    // ----------------------------------------------------
    // 1. 속성 (Field)
    // ----------------------------------------------------
    private String name;
    private int price;

    private int hungerEffect;  // 배고픔 변화
    private int healthEffect;  // 건강 변화
    private int moodEffect;    // 기분 변화
    private int energyEffect;  // 에너지 변화

    // ----------------------------------------------------
    // 2. 생성자
    // ----------------------------------------------------
    public Item(String name, int price, int hunger, int health, int mood, int energy) {
        this.name = name;
        this.price = price;
        this.hungerEffect = hunger;
        this.healthEffect = health;
        this.moodEffect = mood;
        this.energyEffect = energy;
    }

    // ----------------------------------------------------
    // 3. 아이템 적용
    // ----------------------------------------------------
    public boolean use(ImaginaryBugi bugi) {
        // 상태 변화 적용
        StatusGauge gauge = bugi.getGauge();

        gauge.setHunger(gauge.getHunger() + hungerEffect);
        gauge.setHealth(gauge.getHealth() + healthEffect);
        gauge.setMood(gauge.getMood() + moodEffect);
        gauge.setEnergy(gauge.getEnergy() + energyEffect);

        System.out.println(name + "을(를) 사용했습니다!");
        return true;
    }

    // ----------------------------------------------------
    // 4. Getter
    // ----------------------------------------------------
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    // ----------------------------------------------------
    // 5. toString()
    // ----------------------------------------------------
    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", hunger=" + hungerEffect +
                ", health=" + healthEffect +
                ", mood=" + moodEffect +
                ", energy=" + energyEffect +
                '}';
    }
}

