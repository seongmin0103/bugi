
// package bugi;

public class Place {

    // ----------------------------------------------------
    // 1. 속성 (Field)
    // ----------------------------------------------------
    private String name;   // 집, 학교, 카페, 병원 등
    private String action; // 수행할 행동 이름 (sleep, eat, study, work...)

    // ----------------------------------------------------
    // 2. 생성자
    // ----------------------------------------------------
    public Place(String name, String action) {
        this.name = name;
        this.action = action;
    }

    // ----------------------------------------------------
    // 3. 행동 수행
    // ----------------------------------------------------
    public void performAction(ImaginaryBugi bugi) {
        System.out.println("장소: " + name + " 에서 행동을 수행합니다.");

        switch(action) {
            case "sleep":
                bugi.sleep();
                break;

            case "eat":
                // eat은 아이템 시스템으로 처리 가능하므로
                // 여기서는 단순 호출만 가능하게 해둠.
                System.out.println("집에서 밥을 먹으려면 Item을 사용해야 합니다!");
                break;

            case "study":
                bugi.study();
                break;

            case "work":
                bugi.work();
                break;

            case "exercise":
                bugi.exercise();
                break;

            case "wash":
                bugi.wash();
                break;

            case "heal":
                bugi.heal();
                break;

            default:
                System.out.println("이 장소에서는 아무 행동도 할 수 없습니다.");
        }
    }

    // ----------------------------------------------------
    // 4. Getter
    // ----------------------------------------------------
    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "Place{name='" + name + "', action='" + action + "'}";
    }
}

