
// package bugi;

public class ImaginaryBugi {

    // ----------------------------------------------------
    // 1. 속성 (Field)
    // ----------------------------------------------------
    private String name;         // 부기 이름
    private StatusGauge gauge;   // 상태 게이지 (배고픔, 건강, 기분, 에너지)
    private CoinManager coins;   // 코인 관리
    private LevelSystem level;   // 레벨 시스템

    // 공부 누적량 등 레벨업 조건용 값
    private int studyCount;
    private int workCount;
    private int exerciseCount;

    // ----------------------------------------------------
    // 2. 생성자
    // ----------------------------------------------------
    public ImaginaryBugi(String name) {
        this.name = name;
        this.gauge = new StatusGauge();   // 디폴트: 모든 게이지 100
        this.coins = new CoinManager();
        this.level = new LevelSystem();
        this.studyCount = 0;
        this.workCount = 0;
        this.exerciseCount = 0;
    }

    // ----------------------------------------------------
    // 3. Getter
    // ----------------------------------------------------
    public String getName() {
        return name;
    }

    public StatusGauge getGauge() {
        return gauge;
    }

    public CoinManager getCoins() {
        return coins;
    }

    public LevelSystem getLevelSystem() {
        return level;
    }
    
    public int getStudyCount() {
        return studyCount;
    }

    

    // ----------------------------------------------------
    // 4. 행동 메서드
    //  (각 행동은 gauge.updateStatus()에 넘겨서 상태 변화 중)
    // ----------------------------------------------------

    // 잠자기
    public void sleep() {
        System.out.println(name + "이(가) 잠을 잡니다...");
        gauge.updateStatus("sleep");

        // 레벨업 조건 관련
        // 잠자기 자체는 조건에 영향 없음
    }

    // 공부하기 (학교)
    public void study() {
        System.out.println(name + "이(가) 공부합니다...");
        gauge.updateStatus("study");
        studyCount++;
    }

    // 아르바이트 하기 (카페)
    public void work() {
        System.out.println(name + "이(가) 아르바이트를 합니다...");
        gauge.updateStatus("work");
        workCount++;

        // 아르바이트 시 코인 획득
        coins.earn(10);  // 이후 숫자는 조정 가능
    }

    // 운동하기 (공원)
    public void exercise() {
        System.out.println(name + "이(가) 운동합니다...");
        gauge.updateStatus("exercise");
        exerciseCount++;
    }

    // 씻기 (욕실)
    public void wash() {
        System.out.println(name + "이(가) 씻고 있습니다...");
        gauge.updateStatus("wash");
    }

    // 병원 진료 받기
    public void heal() {
        System.out.println(name + "이(가) 병원 진료를 받습니다...");
        gauge.updateStatus("heal");

        // 병원에서는 코인 지출
        coins.spend(20); // 금액은 나중에 조정
    }

    // ----------------------------------------------------
    // 5. 레벨업 체크
    // ----------------------------------------------------
    public void checkLevelUp() {
        boolean levelUp = level.checkLevelUp(this);

        if (levelUp) {
            System.out.println(name + "이(가) 레벨업 했습니다!");
            level.increaseStats();
            gauge.increaseStats(); // 게이지 상한 올리기
        }
    }

    // ----------------------------------------------------
    // 6. toString()
    // ----------------------------------------------------
    @Override
    public String toString() {
        return "ImaginaryBugi{name='" + name + "', " +
                "gauge=" + gauge +
                ", coins=" + coins.getBalance() +
                ", level=" + level.getLevel() + "}";
    }
}
